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
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import java.sql.Date;

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
    private Label aptNo = new Label();

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

    }

    @FXML
    private void submitRequest(ActionEvent event) throws SQLException, IOException {
        if(selectedIssue != null) {
            if(aptNo.getText() != null) {
                if(test) {
                System.out.println("ready to submit request");
                }
                String existQ = "SELECT Request_Date, Issue_Type " +
                                    "FROM Maintenance_Request " +
                                    "WHERE Apt_No = '" + aptnumber + "';";
                Statement getExist = conn.createStatement();
                ResultSet exists = getExist.executeQuery(existQ);
                boolean issueExists = false;

                while (exists.next()) {
                    if ((issueSel.getValue().equals(exists.getString("Issue_Type")))
                            && (Date.valueOf(LocalDate.now()).equals(exists.getDate("Request_Date")))) {
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        Parent root = FXMLLoader.load(
                                    getClass().getResource("IssueExists.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        issueExists = true;
                    }
                }

                if (!issueExists) {
                    String maintQ = "INSERT INTO Maintenance_Request(Request_Date, Apt_No,"
                                        + "Issue_Type) VALUES('" + LocalDate.now()
                                        + "', '" + aptnumber + "', '" + selectedIssue + "');";
                    Statement getMaint = conn.createStatement();
                    getMaint.executeUpdate(maintQ);
                    getMaint.close();

                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    Parent root = FXMLLoader.load(
                            getClass().getResource("MaintRequestMessage.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }

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

}
