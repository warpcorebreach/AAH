/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.*;
import static java.sql.JDBCType.NULL;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class MaintenanceRequestController implements Initializable {
    private Connection conn;
    private String selectedIssue, user;
    private List<String> issues;
    private int aptnumber;
    private boolean test;

    @FXML
    private Button submit = new Button();

    @FXML
    private Label curDate = new Label();

    @FXML
    private TextField aptNo = new TextField();

    @FXML
    private ChoiceBox issueSel = new ChoiceBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Tables.getConnection();
        // get currently logged in user
        user = Tables.getCurrentUser();
        // set request date label to display current date
        curDate.setText("Request Date: " + LocalDate.now());
        // get current user's apartment number to populate apt no field
        try {
            String apt = "SELECT A.Apt_No, Rent " +
                        "FROM Apartment A JOIN Resident R " +
                        "ON A.Apt_No = R.Apt_No " +
                        "WHERE Username = '" + user + "'";
            Statement getApt = conn.createStatement();
            ResultSet finalApt = getApt.executeQuery(apt);
            if (finalApt.next()) {
                aptnumber = finalApt.getInt("Apt_No");
            }
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
        aptNo.setText("" + aptnumber);

        issues = new ArrayList<>();
        try {
            String issue = "SELECT *"
                            + "FROM Issue";
            Statement getIssue = conn.createStatement();
            ResultSet finalIssue = getIssue.executeQuery(issue);
            while (finalIssue.next()) {
                issues.add(finalIssue.getString("Issue_Type"));
            }
            issueSel.setItems(FXCollections.observableArrayList(issues));
        } catch(SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
//        issues.add("Garbage Disposal");
//        issues.add("Roaches");
//        issues.add("Door lock");
//        issues.add("Toilet clogged");
//        issues.add("Drain clogged");

    }

    @FXML
    private void submitRequest(ActionEvent event) throws SQLException {
        if(selectedIssue != null) {
            if(aptNo.getText() != null) {
                String s = aptNo.getText();
                try {
                    aptnumber = Integer.parseInt(s);
                    test = true;
                } catch (NumberFormatException e) {
                        System.out.println("Please enter an Valid Apt Number");
                        test = false;
                }
                if(test) {
                System.out.println("ready to submit request");
                }
                String maintQ = "INSERT INTO Maintenance_Request VALUES" 
                                    + "('" + LocalDate.now() + "', '" + NULL 
                                    + "', '" + aptnumber + "', '" + selectedIssue + "')";
                Statement getMaint = conn.createStatement();
                getMaint.executeUpdate(maintQ);
                getMaint.close();
                
            } else {
                System.out.println("Please enter an Apt Number");
            }
        } else {
            System.out.println("Please selec a Issue");
        }
    }

    @FXML
    private void selectIssue() {
        issueSel.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) {
                    String i = issues.get(newVal.intValue());
                    selectedIssue = i;
                }
            });
    }

    private String getUser() throws SQLException {
        Statement getApt = conn.createStatement();
        ResultSet apt = getApt.executeQuery("SELECT Apt_No AS A "
            + "FROM USER JOIN RESIDENT ON USER.Apt_No = RESIDENT.Apt_No "
            + "WHERE Username = '" + user + "'");
        return "";
    }
    
    @FXML
    private void loadHomepage(ActionEvent event) throws IOException, SQLException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(
                    getClass().getResource("ResidentHomepage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
   }
}
