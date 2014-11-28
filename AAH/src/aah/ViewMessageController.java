/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class ViewMessageController implements Initializable {
    
    private String curUser;
    @FXML
    private Label message = new Label();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection conn = Tables.getConnection();
        curUser = Tables.getCurrentUser();
        try {
            String remQ = "SELECT Date, Message, Apt_No "
                            + "FROM Reminder JOIN Resident "
                            + "ON Reminder.Apt_No = Resident.Apt_No"
                            + "WHERE Username = '" + curUser + "';";

            Statement getRem = conn.createStatement();
            ResultSet rem = getRem.executeQuery(remQ);
            rem.next();

            message.setText(rem.getString("Message"));
            
            String delQ = "DELETE FROM Reminder "
                        + "WHERE Date = '" + rem.getDate("Date")
                        + "AND Apt_No = '" + rem.getInt("Apt_No") + "';";
                        Statement del = conn.createStatement();
                        del.executeUpdate(delQ);
                        del.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }    
    
}
