/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class MgrHomepageController implements Initializable {
    private List<String> reports;
    private String selected;

    @FXML
    private Button rentButton = new Button();

    @FXML
    private Button maintButton = new Button();

    @FXML
    private Button appButton = new Button();

    @FXML
    private Button viewButton = new Button();

    @FXML
    private ChoiceBox reportChoice = new ChoiceBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reports = new ArrayList<>();
        reports.add("Leasing Report");
        reports.add("Service Request Resolution Report");
        reports.add("Rent Defaulter Report");

        selected = null;

        reportChoice.setItems(FXCollections.observableArrayList(reports));
    }

    @FXML
    private void rentReminders(ActionEvent event) {
        System.out.println("Go to rent reminders screen.");
    }

    @FXML
    private void maintRequests(ActionEvent event) {
        System.out.println("Go to maintenance requests screen.");
    }

    @FXML
    private void appReview(ActionEvent event) {
        System.out.println("Go to application review screen.");
    }

    @FXML
    private void viewReport(ActionEvent even) {
        if (selected == null) {
            System.out.println("Please choose a report to view.");
        } else {
            System.out.println("Viewing " + selected);
        }
    }

    @FXML
    private void select() {
        reportChoice.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) {
                    String s = reports.get(newVal.intValue());
                    selected = s;
                }
            });
    }

}
