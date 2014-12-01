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
import javafx.scene.control.cell.PropertyValueFactory;
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
    private final String LEASE_REPORT_QUERY = "SELECT Month, Category, Count(P.Apt_No) AS apt_count " +
                                                "FROM Pays_Rent P JOIN Apartment A " +
                                                "ON P.Apt_No = A.Apt_No " +
                                                "GROUP BY Month, Category " +
                                                "HAVING Month = 'August' OR Month = 'September' OR Month = 'October';";
    private final String EMPTY_QUERY_MSG = "NOTICE: No apartments were leased for the months of August, September, and October.";

    //database connection
    private Connection conn;
    //table that holds lease report
    @FXML
    private final TableView leaseReport = new TableView();

    private ObservableList<LeaseReportEntry> data;

    @FXML
    private final Button back = new Button();
    @FXML
    private final TableColumn monthCol = new TableColumn();
    @FXML
    private final TableColumn categoryCol = new TableColumn();
    @FXML
    private final TableColumn numberCol = new TableColumn();
    @FXML
    private final Label messages = new Label();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if(DEBUG){
            System.out.print("getting database connection... ");
        }//end if
        //get the database connection
        conn = Tables.getConnection();
        //did something go wrong? send them out back to login
        if(conn == null){
            System.out.println("\nERROR: connection object is [null] for scene [LeaseReport]");
            System.out.println("\tHeading back to [Login]");
            //THIS DOESN'T WORK
            showLogin(new ActionEvent());
        }//end if
        //connection is good
        else{
            if(DEBUG){
                System.out.println("done.");
            }//end if
            showLeasingReport();
        }//end else

    }//end initialize

    /**
     * This method auto-generates the leasing report data
     */
    private void showLeasingReport(){
        //data array
        data = FXCollections.observableArrayList();
         try{
                 if(DEBUG){
                    System.out.print("building query... ");
                }
                //make a query object
                Statement query = conn.createStatement();
                if(DEBUG){
                    System.out.print("done.\nexecuting query... ");
                }
                ResultSet res = query.executeQuery(LEASE_REPORT_QUERY);
                if(DEBUG){
                    System.out.println("done.\nlooping through results... ");
                }//end if
                List<LeaseReportEntry> l = new ArrayList<>();
                ObservableList<LeaseReportEntry> data = FXCollections.observableArrayList();
                //this would evaluate to true if no rows were returned since
                //  there would be no next
                if(!res.next()){
                    if(DEBUG){
                        System.out.print(" --RETURNED EMPTY SET-- \nsetting message label text...");
                    }//end if
                    messages.setText(EMPTY_QUERY_MSG);
                    if(DEBUG){
                        System.out.println("done.");
                    }//end if
                }//end if
                //we got some stuff back
                else{
                    int i = 0;
                    //do something with that first row
                    do {
                        l.add(new LeaseReportEntry(
                                res.getString("Month"),
                                res.getString("Category"),
                                res.getString("apt_count")
                            ));
                        System.out.println(l.get(i));
                        i++;
                    }//end do
                    //move on to the next one
                    while(res.next());
                }//end else
                if(DEBUG){
                    System.out.print("done.\nclosing query... ");
                }//end if
                //close the satement
                query.close();
                if(DEBUG){
                    System.out.println("done.\n");
                }//end if
                //set columns
                monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
                categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
                numberCol.setCellValueFactory(new PropertyValueFactory<>("apt_count"));
                //add all the data to the table
                data.addAll(l);
                leaseReport.setItems(data);
            }//end try
            catch(Exception ex){
                System.out.println("\nERROR: could not execute query");
                System.out.println("\t" + ex.getMessage());
            }//end catch
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
