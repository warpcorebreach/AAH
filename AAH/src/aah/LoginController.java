
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

/**
 *
 * @author Justin
 */
public class LoginController implements Initializable {
    private int userType;
    private Connection conn;

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
        conn = Tables.getConnection();
    }

    @FXML
    private void login(ActionEvent event) throws SQLException, IOException {
        if (username.getText().equals("")) {
            System.out.println("Please enter a valid username.");
        } else if (password.getText().equals("")) {
            System.out.println("Please enter a valid password.");
        } else {
            String uname = username.getText();
            System.out.println("Username: " + uname);
            System.out.println("Password: " + password.getText());

            /* check DB for valid username/password combo
            check if user is a manager, resident, or prospective resident
            userType: 1 - manager
                      2 - resident
                      3 - prospective resident */

            String userQ = "SELECT COUNT(*) AS C FROM User WHERE "
                    + "Username = '" + uname + "' "
                    + "AND Password = '" + password.getText() + "';";
            Statement checkUser = conn.createStatement();
            ResultSet user = checkUser.executeQuery(userQ);

            user.next();    // increment ResultSet pointer becuase reasons
            if (user.getInt("C") == 1 ) {
                // we have a valid user, load proper Homepage
                Tables.setCurrentUser(uname);
                loadHomepage(event, uname);
            } else {
                // throw login error message
                System.out.println("You username/password is invalid.");
                System.out.println("Please try again.");
            }
            checkUser.close();

        }
    }

    private void loadHomepage(ActionEvent event, String uname)
            throws IOException, SQLException {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root;

        String homeQ = "SELECT COUNT(*) AS C FROM Management "
                + "WHERE Username = '" + uname + "';";
        Statement checkHome = conn.createStatement();
        ResultSet home = checkHome.executeQuery(homeQ);

        home.next();
        if (home.getInt("C") == 1) {
            // load Manager Homepage
            root = FXMLLoader.load(
                    getClass().getResource("MgrHomepage.fxml"));
        } else {
            homeQ = "SELECT COUNT(*) AS C FROM Resident "
                + "WHERE Username = '" + uname + "'";
            checkHome = conn.createStatement();
            home = checkHome.executeQuery(homeQ);

            home.next();
            if (home.getInt("C") == 1) {
                // load Resident Homepage
                root = FXMLLoader.load(
                    getClass().getResource("ResidentHomepage.fxml"));
            } else {
                // load Prospective Resident Homepage
                root = FXMLLoader.load(
                    getClass().getResource("ProspResidentLogin.fxml"));
            }
        }
        checkHome.close();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

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
