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
    private int apt;
    @FXML
    private Label message = new Label();
    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection conn = Tables.getConnection();
        curUser = Tables.getCurrentUser();
        try {
            String remQ = "SELECT Date, Reminder.Apt_No, Message " 
                            + "FROM Reminder JOIN Resident "
                            + "ON Reminder.Apt_No = Resident.Apt_No "
                            + "WHERE Username = '" + curUser + "';";

            Statement getRem = conn.createStatement();
            ResultSet rem = getRem.executeQuery(remQ);
            rem.next();
            apt = rem.getInt("Reminder.Apt_No");

            message.setText(rem.getString("Message"));
            
            String delQ = "DELETE FROM Reminder "
                        + "WHERE Date = '" + rem.getDate("Date") + "' "
                        + "AND Apt_No = '" + apt + "';";
                        Statement del = conn.createStatement();
                        del.executeUpdate(delQ);
                        del.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }    
    
}
