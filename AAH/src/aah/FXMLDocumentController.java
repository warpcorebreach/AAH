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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Justin
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button newAcctButton = new Button();

    @FXML
    private Button loginButton = new Button();

    @FXML
    private TextField username = new TextField();

    @FXML
    private TextField password = new TextField();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        if (username.getText().equals("")) {
            System.out.println("Please enter a valid username.");
        } else if (password.getText().equals("")) {
            System.out.println("Please enter a valid password.");
        } else {
            System.out.println("Username: " + username.getText());
            System.out.println("Password: " + password.getText());

            // check DB for valid username/password combo

            /* go to Homepage */
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("ResidentHomepage.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void createAccount(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
