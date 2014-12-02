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
    private Button backButton = new Button();

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
    private int apt, rent, feet, newResPrefLease;
    private String cat, curName, newResUName;
    private Date avail;
    private AllotmentEntry selected;
    private Connection conn;

    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            newResUName = Tables.getNewResident();
            curName = Tables.getCurName();
            List<AllotmentEntry> entries = new ArrayList<>();

            String allQ = "SELECT Apt_No, A.Category, Rent, Sq_Ft, Available_On, Name, Pref_Lease " +
                            "FROM Apartment A JOIN Prospective_Resident P " +
                            "ON A.Category = P.Category " +
                            "WHERE Move_Date >= Available_On " +
                            "AND Name = '" + curName + "' " +
                            "AND Rent >= P.Min_Rent AND Rent <= P.Max_Rent " +
                            "ORDER BY Rent ASC";
            conn = Tables.getConnection();
            Statement getAll = conn.createStatement();
            ResultSet allot = getAll.executeQuery(allQ);

            int i = 0;
            while (allot.next()) {
                apt = allot.getInt("Apt_No");
                cat = allot.getString("A.Category");
                rent = allot.getInt("Rent");
                feet = allot.getInt("Sq_Ft");
                avail = allot.getDate("Available_On");

                if (i == 0) newResPrefLease = allot.getInt("Pref_Lease");

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
                new PropertyValueFactory<>("avail"));

            nameLabel.setText(curName);
            data.addAll(entries);
            table.setItems(data);

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void assign(ActionEvent event)throws IOException, SQLException {
        // insert the new resident
        String assQ = "INSERT INTO Resident VALUES ('" + newResUName + "', " +
                selected.getApt() + ")";

        Statement assRes = conn.createStatement();
        assRes.executeUpdate(assQ);
        assRes.close();

        // get the new available date for the selected apartment
        // (this is much, much cleaner than having it in one statement)
        String dateQ = "SELECT DATE_ADD(Available_On, " +
                        "INTERVAL " + newResPrefLease + " MONTH) AS D " +
                        "FROM Apartment " +
                        "WHERE Apt_No = " + selected.getApt();

        Statement getDate = conn.createStatement();
        ResultSet date = getDate.executeQuery(dateQ);
        date.next();
        Date newDate = date.getDate("D");
        System.out.println(newDate);

        // finally update the entry in Apartment
        String upQ = "UPDATE Apartment " +
                "SET Available_On = '" + newDate + "'" +
                ", Lease_Term = " + newResPrefLease +
                " WHERE Apt_No = " + selected.getApt();
        Statement upRes = conn.createStatement();
        upRes.executeUpdate(upQ);
        upRes.close();
    }

    @FXML
    private void select() {
        selected = (AllotmentEntry) table.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root;
        root = FXMLLoader.load(
                getClass().getResource("AppReview.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
