/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

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

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class MaintenanceRequestController implements Initializable {
    private Connection conn;
    private String selectedIssue, user;
    private List<String> issues;

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

        issues = new ArrayList<>();
        issues.add("Garbage Disposal");
        issues.add("Roaches");
        issues.add("Door lock");
        issues.add("Toilet clogged");
        issues.add("Drain clogged");

        // get current user's apartment number to populate apt no field

    }

    @FXML
    private void submitRequest(ActionEvent event) {

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
