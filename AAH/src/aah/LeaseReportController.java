/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;


/**
 * FXML Controller class for the leasing report
 *
 * @author Brendan
 */
public class LeaseReportController implements Initializable {
    
    //database connection
    private Connection conn = null;
    //current user
    private String user = null;
    //table that holds lease report
    private TableView leaseReport = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //get the database connection
        conn = Tables.getConnection();
        //did something go wrong? send them out back to login
        if(conn == null){
            
            System.out.println("ERROR: connection object is [null] for scene [LeaseReport]");
            System.out.println("\tHeading back to [Login]");
            
            /*
             * TODO
            */
            try{
                //search for the login homepage
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene scene = new Scene(root);
            }//end try
            catch(IOException ex){
                System.out.println("Cannot find fxml file for login!");
            }//end catch
            
        }//end if
        //connection is good
        else{
            showLeasingReport();
        }//end else
        
    }//end initialize    
    
    private void showLeasingReport(){
        //TODO
    }//end method showLeasingReport
    
}//end class LeaseReportController
