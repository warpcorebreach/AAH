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

            /*****
             * Get accepted applications
             */
            String revQ = "SELECT Name, DOB, Gender, Income, Category, Min_Rent, " +
                "Max_Rent, Move_Date, Pref_Lease, Username " +
                "FROM Prospective_Resident " +
                "WHERE Username NOT IN (SELECT Username FROM Resident) " +
                "AND (Income/3) >= (SELECT MIN(Rent) FROM Apartment A JOIN " +
                "Prospective_Resident P ON A.Category = P.Category) " +
                "AND Move_Date >= (SELECT MIN(Available_on) FROM " +
                "Apartment A JOIN Prospective_Resident P ON " +
                "A.Category = P.Category)";

            Statement getRev = conn.createStatement();
            ResultSet rev = getRev.executeQuery(revQ);

            while(rev.next()) {
                name = rev.getString("Name");
                dob = rev.getDate("DOB");
                gen = rev.getString("Gender");
                income = rev.getInt("Income");
                cat = rev.getString("Category");
                min = rev.getInt("Min_Rent");
                max = rev.getInt("Max_Rent");
                move = rev.getDate("Move_Date");
                term = rev.getString("Pref_Lease");
                user = rev.getString("Username");

                System.out.println(name + " " + user);
                System.out.println("Application Accepted");
                System.out.println();

                apps.add(new ApartmentEntry(name, dob, gen, income, cat, min,
                        max, move, term, user, "Accepted"));
            }
            getRev.close();
            /*****
             * End get accepted applications
             */


            /*****
             * Get rejected applications
             */
            revQ = "SELECT Name, DOB, Gender, Income, Category, Min_Rent, " +
                "Max_Rent, Move_Date, Pref_Lease, Username " +
                "FROM Prospective_Resident " +
                "WHERE Username NOT IN (SELECT Username FROM Resident) " +
                "AND (Income/3) < (SELECT MIN(Rent) FROM Apartment A JOIN " +
                "Prospective_Resident P ON A.Category = P.Category) " +
                "OR Move_Date < (SELECT MIN(Available_on) FROM " +
                "Apartment A JOIN Prospective_Resident P ON " +
                "A.Category = P.Category)";

            getRev = conn.createStatement();
            rev = getRev.executeQuery(revQ);

            while(rev.next()) {
                name = rev.getString("Name");
                dob = rev.getDate("DOB");
                gen = rev.getString("Gender");
                income = rev.getInt("Income");
                cat = rev.getString("Category");
                min = rev.getInt("Min_Rent");
                max = rev.getInt("Max_Rent");
                move = rev.getDate("Move_Date");
                term = rev.getString("Pref_Lease");
                user = rev.getString("Username");

                System.out.println(name + " " + user);
                System.out.println("Application Rejected");
                System.out.println();

                apps.add(new ApartmentEntry(name, dob, gen, income, cat, min,
                        max, move, term, user, "Rejected"));
            }
            getRev.close();
            /*****
             * End get rejected applications
             */


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
        }
    }

    @FXML
    private void accept(ActionEvent event) throws IOException {
        if (selected.getStatus().equals("Rejected")) {
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
