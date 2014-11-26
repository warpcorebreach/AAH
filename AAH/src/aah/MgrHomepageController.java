/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

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
    private void rentReminders(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root;
        root = FXMLLoader.load(
                getClass().getResource("Reminder.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    private void viewReport(ActionEvent event) throws IOException {
        if (selected == null) {
            System.out.println("Please choose a report to view.");
        } else {
            System.out.println("Viewing " + selected);
            Parent root;
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            if (selected == "Leasing Report") {
                root = FXMLLoader.load(
                        getClass().getResource("LeaseReport.fxml"));
            } else if (selected == "Service Request Resolution Report") {
                root = FXMLLoader.load(
                        getClass().getResource("ServiceRequestReport.fxml"));
            } else {
                root = FXMLLoader.load(
                        getClass().getResource("RentDefaultReport.fxml"));
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
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
