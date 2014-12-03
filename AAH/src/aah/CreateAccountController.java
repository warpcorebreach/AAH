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
import java.sql.*;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;


/**
 * FXML Controller class
 *
 * @author Justin
 */
public class CreateAccountController implements Initializable {
    private Connection conn;

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
        conn = Tables.getConnection();
    }

    @FXML
    private void register(ActionEvent event) throws IOException, SQLException {
        if (username.getText().equals("")) {
            System.out.println("Please enter a valid username.");
        } else if (password.getText().equals("")) {
            System.out.println("Please enter a valid password.");
        } else if (!password.getText().equals(passwordConf.getText())) {
            System.out.println("Password and Confirm Password must match.");
        } else {
            String uname = username.getText();
            String pword = password.getText();
            System.out.println("Username: " + uname);
            System.out.println("Password: " + pword);

            // check DB for valid username
            // add new info to DB

            String userQ = "SELECT COUNT(*) AS C FROM User WHERE "
                    + "Username = '" + uname + "';";
            Statement checkUser = conn.createStatement();
            ResultSet user = checkUser.executeQuery(userQ);
            user.next();

            if (user.getInt("C") == 1) {
                System.out.println("That username has already been registered.");
            } else {
                userQ = "INSERT INTO User VALUES('" + uname + "', '" + pword
                        + "');";
                checkUser = conn.createStatement();
                checkUser.executeUpdate(userQ);

                // set current user
                Tables.setCurrentUser(uname);

                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                Parent root = FXMLLoader.load(
                        getClass().getResource("ApplicationForm.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            checkUser.close();
        }

    }

}
