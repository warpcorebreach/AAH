
package aah;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/******************************************************************************
 * This class initializes the DB connection and creates the tables via the
 * static methods in Tables. Also adds a few entries to User for testing.
 *
 * Notes:
 *  1) comment out the line with Tables.dropTables() if the DB is empty
 *  2) as of now only the login screen uses DB functionality but it works
 *  3) login screen can be tested by logging in as one of the users below
 *     (there is one user in the DB for each type)
 *
 * Users:
 *  1) Username: jcoates8
 *     Password: 1234
 *     Type: Management
 *
 *  2) Username: jtrimm3
 *     Password: 1234
 *     Type: Resident
 *
 *  3) Username: wli
 *     Password: 1234
 *     Type: Prospective Resident
 *
 * @author Justin
 *****************************************************************************/
public class AAH extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("Login.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        boolean success = Tables.initConnection();
        if (success) {
            System.out.println("Connection successful!");
            //Tables.dropTables();
            Tables.createTables();
            launch(args);
        } else {
            System.out.println("Connection failed.");
            return;
        }
    }

}
