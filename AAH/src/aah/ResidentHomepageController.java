/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class ResidentHomepageController implements Initializable {
    private int numMessages;

    @FXML
    private Hyperlink messages = new Hyperlink();

    @FXML
    private Button rentButton = new Button();

    @FXML
    private Button maintButton = new Button();

    @FXML
    private Button paymentInfoButton = new Button();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numMessages = 5;
        if (numMessages == 0) {
            messages.setVisible(false);
        } else {
            messages.setText("You have " + numMessages + " unread message(s)"
                    + " from Management.");
            messages.setVisible(true);
        }
    }

    @FXML
    private void viewMessages(ActionEvent event) {
        System.out.println("Go to messages screen.");
    }

    @FXML
    private void payRent(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root;
        root = FXMLLoader.load(
                getClass().getResource("RentPayment.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void maintRequest(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root;
        root = FXMLLoader.load(
                getClass().getResource("MaintenanceRequest.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
