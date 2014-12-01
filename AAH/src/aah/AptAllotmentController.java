/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class AptAllotmentController implements Initializable {

    @FXML
    private Button allotButton = new Button();

    @FXML
    private Label nameLabel = new Label();

    @FXML
    private TableView table = new TableView();

    @FXML
    private TableColumn aptCol = new TableColumn();

    @FXML
    private TableColumn catCol = new TableColumn();

    @FXML
    private TableColumn rentCol = new TableColumn();

    @FXML
    private TableColumn feetCol = new TableColumn();

    @FXML
    private TableColumn dateCol = new TableColumn();

    private ObservableList<AllotmentEntry> data
            = FXCollections.observableArrayList();
    private int apt, rent, feet;
    private String cat, curName;
    private Date avail;

    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            curName = Tables.getCurName();
            List<AllotmentEntry> entries = new ArrayList<>();

            String allQ = "SELECT Apt_No, A.Category, Rent, Sq_Ft, Available_On, Name " +
                            "FROM Apartment A JOIN Prospective_Resident P " +
                            "ON A.Category = P.Category " +
                            "WHERE Rent >= Min_Rent AND Rent <= Max_Rent "+//AND Move_Date >= Available_On " +
                            //"AND (Pref_Lease = Lease_Term OR Lease_Term = NULL) AND Name = '" + curName + "' " +
                            "AND Name = '" + curName + "' " +
                            "ORDER BY Rent DESC";
            Connection conn = Tables.getConnection();
            Statement getAll = conn.createStatement();
            ResultSet allot = getAll.executeQuery(allQ);

            int i = 0;
            while (allot.next()) {
                apt = allot.getInt("Apt_No");
                cat = allot.getString("A.Category");
                rent = allot.getInt("Rent");
                feet = allot.getInt("Sq_Ft");
                avail = allot.getDate("Available_On");

                entries.add(new AllotmentEntry(apt, cat, rent, feet, avail));
                System.out.println(entries.get(i));
                i++;
            }

            aptCol.setCellValueFactory(
                new PropertyValueFactory<>("apt"));
            catCol.setCellValueFactory(
                new PropertyValueFactory<>("cat"));
            rentCol.setCellValueFactory(
                new PropertyValueFactory<>("rent"));
            feetCol.setCellValueFactory(
                new PropertyValueFactory<>("feet"));
            dateCol.setCellValueFactory(
                new PropertyValueFactory<>("date"));

            nameLabel.setText(curName);
            data.addAll(entries);
            table.setItems(data);

//            try {
//                ActionEvent event = null;
//                Node node = (Node) event.getSource();
//                Stage stage = (Stage) node.getScene().getWindow();
//                Parent root;
//                root = FXMLLoader.load(
//                        getClass().getResource("AptAllotment.fxml"));
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//            } catch (IOException ex) {
//                System.out.println("IO Error: " + ex.getMessage());
//            }

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void assign(ActionEvent event)throws IOException, SQLException {
        String assQ = "INSERT INTO Resident VALUES ($username, $apt_no);";
        Connection conn = Tables.getConnection();
        Statement assRes = conn.createStatement();
        assRes.executeUpdate(assQ);
        assRes.close();

        String upQ = "UPDATE APARTMENT " +
                        "SET Available_On = " +
                        "DATE_ADD((SELECT Available_On " +
                        " FROM APARTMENT A JOIN RESIDENT R " +
                        " ON A.Apt_No = R.Apt_No " +
                        " WHERE Username = $username), " +
                        "INTERVAL (SELECT Pref_Lease " +
                        " FROM APARTMENT A JOIN RESIDENT R " +
                        " ON A.Apt_No = R.Apt_No\n" +
                        " WHERE Username = $username) MONTH) " +
                        "WHERE Apt_No = $apt_no;";
        Statement upRes = conn.createStatement();
        upRes.executeUpdate(upQ);
        upRes.close();
        conn.close();
    }

}
