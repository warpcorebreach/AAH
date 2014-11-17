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
 * FXML Controller class
 *
 * @author Justin
 */
public class CreateAccountController implements Initializable {

    @FXML
    private Button regButton = new Button();

    @FXML
    private TextField username = new TextField();

    @FXML
    private TextField password = new TextField();

    @FXML
    private TextField passwordConf = new TextField();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void register(ActionEvent event) throws IOException {
        if (username.getText().equals("")) {
            System.out.println("Please enter a valid username.");
        } else if (password.getText().equals("")) {
            System.out.println("Please enter a valid password.");
        } else if (!password.getText().equals(passwordConf.getText())) {
            System.out.println("Password and Confirm Password must match.");
        } else {
            System.out.println("Username: " + username.getText());
            System.out.println("Password: " + password.getText());

            // check DB for valid username/password combo
            // add new info to DB

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("ApplicationForm.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

}
