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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class AppReviewController implements Initializable {

    @FXML
    private Button approveButton = new Button();

    @FXML
    private Button backButton = new Button();

    @FXML
    private TableView table = new TableView();

    @FXML
    private TableColumn nameCol = new TableColumn();

    @FXML
    private TableColumn dobCol = new TableColumn();

    @FXML
    private TableColumn genCol = new TableColumn();

    @FXML
    private TableColumn inCol = new TableColumn();

    @FXML
    private TableColumn typeCol = new TableColumn();

    @FXML
    private TableColumn moveCol = new TableColumn();

    @FXML
    private TableColumn leCol = new TableColumn();

    @FXML
    private TableColumn statusCol = new TableColumn();

    // contains ApartmentEntry objects which can be added to a TableView
    private ObservableList<ApartmentEntry> data
            = FXCollections.observableArrayList();

    private String name;
    private Date dob;
    private String gen;
    private int income;
    private String cat;
    private int min;
    private int max;
    private Date move;
    private String term;
    private String user;

    private Connection conn;
    private ApartmentEntry selected;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {   //we should fix this query AND WHILE LOOP
            conn = Tables.getConnection();
            List<ApartmentEntry> apps = new ArrayList<>();

            // get all the prospective residents who have not been approved
            String prosQ = "SELECT Name, DOB, Gender, Income, Category, " +
                    "Min_Rent, Max_Rent, Move_Date, Pref_Lease, Username " +
                    "FROM Prospective_Resident " +
                    "WHERE Username NOT IN (SELECT Username FROM Resident)";
            Statement getPros = conn.createStatement();
            ResultSet prosps = getPros.executeQuery(prosQ);

            List<ProspResident> res = new ArrayList<>();
            ProspResident pr;
            while(prosps.next()) {
                pr = new ProspResident();
                pr.setName(prosps.getString("Name"));
                pr.setGender(prosps.getString("Gender"));
                pr.setCat(prosps.getString("Category"));
                pr.setLease(prosps.getString("Pref_Lease"));
                pr.setUser(prosps.getString("Username"));
                pr.setMin(prosps.getInt("Min_Rent"));
                pr.setMax(prosps.getInt("Max_Rent"));
                pr.setDob(prosps.getDate("DOB"));
                pr.setMove(prosps.getDate("Move_Date"));
                pr.setIncome(prosps.getInt("Income"));
                res.add(pr);
            }
            getPros.close();

            String appQ;
            Statement getApps;
            ResultSet rev;
            int count;
            String status;

            // for each valid prosp resident, see if there is an available
            // apartment
            for (ProspResident r : res) {
                appQ = "SELECT COUNT(*) AS C FROM Apartment " +
                        "WHERE Available_On <= '" + r.getMove() + "' " +
                        "AND (Rent*3) <= " + r.getIncome() +
                        " AND Category = '" + r.getCat() + "' " +
                        "AND Rent >= " + r.getMin() +
                        " AND Rent <= " + r.getMax();
                getApps = conn.createStatement();
                rev = getApps.executeQuery(appQ);

                rev.next();
                count = rev.getInt("C");
                getApps.close();
                name = r.getName();
                dob = r.getDob();
                gen = r.getGender();
                income = r.getIncome();
                cat = r.getCat();
                min = r.getMin();
                max = r.getMax();
                move = r.getMove();
                term = r.getLease();
                user = r.getUser();

                if (count > 0) {
                    status = "Accepted";
                } else {
                    status = "Rejected";
                }

                System.out.println(name + " " + user + " " + move);
                System.out.println("Application " + status);
                System.out.println();

                apps.add(new ApartmentEntry(name, dob, gen, income, cat, min,
                        max, move, term, user, status));
            }

            nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
            dobCol.setCellValueFactory(
                new PropertyValueFactory<>("dob"));
            genCol.setCellValueFactory(
                new PropertyValueFactory<>("gen"));
            inCol.setCellValueFactory(
                new PropertyValueFactory<>("income"));
            typeCol.setCellValueFactory(
                new PropertyValueFactory<>("cat"));
            moveCol.setCellValueFactory(
                new PropertyValueFactory<>("move"));
            leCol.setCellValueFactory(
                new PropertyValueFactory<>("term"));
            statusCol.setCellValueFactory(
                new PropertyValueFactory<>("status"));

            data.addAll(apps);
            table.setItems(data);

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void accept(ActionEvent event) throws IOException {
        if (selected == null) {
            System.out.println("Please select an application.");
        } else if (selected.getStatus().equals("Rejected")) {
            System.out.println("This applications has been rejected by our " +
                    "system.");
        } else {
            System.out.println();
            System.out.println("===== Application Approved =====");
            System.out.println();
            System.out.println(selected.getCat());
            System.out.println(selected.getDob());
            System.out.println(selected.getGen());
            System.out.println(selected.getIncome());
            System.out.println(selected.getMax());
            System.out.println(selected.getMin());
            System.out.println(selected.getMove());
            System.out.println(selected.getName());
            System.out.println(selected.getTerm());
            System.out.println(selected.getUser());

            Tables.setCurName(selected.getName());
            Tables.setNewResident(selected.getUser());

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(
                    getClass().getResource("AptAllotment.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    @FXML
    private void select() {
        selected = (ApartmentEntry) table.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(
                    getClass().getResource("MgrHomepage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }

}
