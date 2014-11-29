/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;


/**
 * FXML Controller class for the leasing report
 *
 * @author Brendan
 */
public class LeaseReportController implements Initializable {
    
    
    //DEBUG MODE- set this to true to get debug prints
    private final boolean DEBUG = true;
    
    //SQL queries
    private final String QUERY_AUGUST = "SELECT * FROM ";
    private final String QUERY_SEPTEMBER = "";
    private final String QUERY_OCTOBER = "";
    
    
    //database connection
    private Connection conn = null;
    //current user
    private String user = null;
    //table that holds lease report
    private TableView leaseReport = new TableView();
    private ObservableList<ObservableList> data;
    //button to redirect back to the manager's homepage    
    @FXML
    private Button back = new Button();
    
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
            //THIS DOESN'T WORK
            showLogin(new ActionEvent());          
        }//end if
        //connection is good
        else{
            showLeasingReport(new ActionEvent());
        }//end else        
    }//end initialize    
    
    /**
     * This method auto-generates the leasing report data
     */
    private void showLeasingReport(ActionEvent event){
        //TODO
    }//end method showLeasingReport
    
    /**
     * This method redirects the user back to the login page
     */
    private void showLogin(ActionEvent event){
        if(DEBUG){
            System.out.println("[BEGIN showLogin()]");
        }//end if
        try{
            if(DEBUG){
                System.out.println("getting stage...");
            }
            //get the stage
            Stage s = Tables.getStage();
            //make sure nothing went wrong here...
            if(s != null){
                if(DEBUG){
                    System.out.println("loading Login.fxml...");
                }//end if
                //redirect over to the manager's home
                Parent p = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene sc = new Scene(p);
                s.setScene(sc);
                if(DEBUG){
                    System.out.println("done. displaying scene...\n[END showLogin()]");
                }//end if
                s.show();
            }//end if
            else{
                System.out.println("ERROR: call to Tables.getStage() returned null reference");
                System.out.println("\tcannot redirect.");
                if(DEBUG){
                    System.out.println("[END showLogin()]");
                }//end if
            }//end else
        }//end try
        //uh-oh... something went wrong
        catch(Exception ex){
            System.out.println("ERROR: could not redirect to manager's homepage");
            System.out.println("\t" + ex.getMessage());
            if(DEBUG){
                    System.out.println("[END showLogin()]");
                }//end if
        }//end catch
    }//end method showLogin
    
    @FXML
    private void showManagerHome(ActionEvent event){
        if(DEBUG){
            System.out.println("[BEGIN showManagerHome()]");
            System.out.println("back button pressed");
        }//end if
        try{
            if(DEBUG){
                System.out.println("getting stage...");
            }
            //get the stage
            Stage s = Tables.getStage();
            //make sure nothing went wrong here...
            if(s != null){
                if(DEBUG){
                    System.out.println("loading MgrHomepage.fxml...");
                }//end if
                //redirect over to the manager's home
                Parent p = FXMLLoader.load(getClass().getResource("MgrHomepage.fxml"));
                Scene sc = new Scene(p);
                s.setScene(sc);
                if(DEBUG){
                    System.out.println("done. displaying scene...\n[END showManagerHome()]");
                }//end if
                s.show();
            }//end if
            else{
                System.out.println("ERROR: call to Tables.getStage() returned null reference");
                System.out.println("\tcannot redirect.");
                if(DEBUG){
                    System.out.println("[END showManagerHome()]");
                }//end if
            }//end else
        }//end try
        //uh-oh... something went wrong
        catch(Exception ex){
            System.out.println("ERROR: could not redirect to manager's homepage");
            System.out.println("\t" + ex.getMessage());
            if(DEBUG){
                    System.out.println("[END showManagerHome()]");
                }//end if
        }//end catch
    }//end method showManagerHome
            
}//end class LeaseReportController
