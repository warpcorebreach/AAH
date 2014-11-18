/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.IOException;
import java.sql.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Justin
 */
public class AAH extends Application {
    private static final String username = "cs4400_Group_16";
    private static final String pword = "bigADQY9";

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        /******************************************************
         * TODO: Implement CREATE TABLE and DROP TABLE statements
         * in a separate class with static methods that can be called
         * from main(). This will make setting up and clearing the DB
         * a little clearer.
         ******************************************************/

        /* initialize database connection */
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://"
                    + "academic-mysql.cc.gatech.edu/cs4400_Group_16",
                    username, pword);
            if (!conn.isClosed()) {
                System.out.println("Successfully connected!");
            }
        } catch (Exception e) {
            System.out.println("Exception caught");
        } /* finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {}
        } */

        if (conn != null) {
            /* DROP TABLE User */
            /*
            Statement sttable1 = conn.createStatement();
            sttable1.executeUpdate("DROP TABLE User");
            System.out.println("Dropping TABLE User...");
            sttable1.close(); */

            /* Insert a new User into the DB */
            /*
            Statement sttable = conn.createStatement();
            sttable.executeUpdate("CREATE TABLE User("
                + "Username VARCHAR(15) NOT NULL,"
                + "Password VARCHAR(20) NOT NULL,"
                + "PRIMARY KEY(Username))");
            sttable.close();

            Statement stinsert = conn.createStatement();
            stinsert.executeUpdate("INSERT INTO User VALUES("
                    + "'jcoates8', '1234')");
            stinsert.close();

            Statement stselect = conn.createStatement();
            ResultSet user = stselect.executeQuery("SELECT * FROM User");
            while (user.next()) {
                System.out.println(user.getString("Username") + ", "
                    + user.getString("Password"));
            }
            user.close(); */
        } else {
            System.out.println("Connection failed.");
        }

        launch(args);
    }

}
