/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.net.*;
import java.sql.*;
import java.util.*;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class ServiceRequestReportController implements Initializable {

    //DEBUG mode- switch to true to get debug prints
    private final boolean DEBUG = true;

    //DB connection
    private Connection conn = null;
    /*
    //data from the SQL queries
    private ObservableList<ServiceRequestReportEntry> data = null;
    */
    //SQL queries
    private final String SERVICE_REQUEST_REPORT_QUERY = "SELECT EXTRACT(MONTH FROM Request_Date) AS Res_Month, Issue_Type, " +
                                                        "AVG(DATEDIFF(Resolved_Date, Request_Date)+1) AS resolved_time " +
                                                        "FROM Maintenance_Request " +
                                                        "WHERE Resolved_Date IS NOT NULL " +
                                                        "GROUP BY Res_Month, Issue_Type " +
                                                        "HAVING Res_Month >= 8 AND Res_Month <= 10;";
    //string for error message
    private final String EMPTY_QUERY_MSG = "NOTICE: No service requests have been resolved for the months of August, September, and October.";
    /*
    @FXML
    private final TableView table = new TableView();
    @FXML
    private TableColumn monthCol = null;
    @FXML
    private TableColumn reqCol = null;
    @FXML
    private TableColumn dayCol = null;
    @FXML
    private final Button back = new Button();
    */
    @FXML
    private final Label messages = new Label();
    @FXML
    private TableView table = new TableView();
    @FXML
    private TableColumn monthCol = new TableColumn();
    @FXML
    private TableColumn reqCol = new TableColumn();
    @FXML
    private TableColumn dayCol = new TableColumn();

    private ObservableList<ServiceRequestReportEntry> data = FXCollections.observableArrayList();
    private String month;
    private String req;
    private String day;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        if(DEBUG){
            System.out.print("getting database connection... ");
        }//end if
        //get the database connection
        conn = Tables.getConnection();
        //sanity check
        if(conn != null){
            if(DEBUG){
                System.out.println("done.");
            }//end if
            try{
                 if(DEBUG){
                    System.out.print("building query... ");
                }
                //make a query object
                Statement query = conn.createStatement();
                if(DEBUG){
                    System.out.print("done.\nexecuting query... ");
                }
                ResultSet res = query.executeQuery(SERVICE_REQUEST_REPORT_QUERY);
                if(DEBUG){
                    System.out.println("done.\nlooping through results... ");
                }//end if
                List<ServiceRequestReportEntry> l = new ArrayList<>();
                ObservableList<ServiceRequestReportEntry> data = FXCollections.observableArrayList();
                //this would evaluate to true if no rows were returned since
                //  there would be no next
                if(!res.next()){
                    if(DEBUG){
                        System.out.print(" --RETURNED EMPTY SET-- \nsetting message label text... ");
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
                        l.add(new ServiceRequestReportEntry(
                                res.getString("Res_Month"),
                                res.getString("Issue_Type"),
                                res.getString("resolved_time")
                            ));
                        System.out.println("[" + i + "]: " + l.get(i));
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
                monthCol = new TableColumn();
                reqCol = new TableColumn();
                dayCol = new TableColumn();
                monthCol.setCellValueFactory(new PropertyValueFactory<>("res_month"));
                reqCol.setCellValueFactory(new PropertyValueFactory<>("Issue_Type"));
                dayCol.setCellValueFactory(new PropertyValueFactory<>("resolved_time"));
                //add all the data to the table
                data.addAll(l);
                table.setItems(data);
            }//end try
            catch(Exception ex){
                System.out.println("\nERROR: could not execute query");
                System.out.println("\t" + ex.getMessage());
            }//end catch
        }//end if
        //connection object is null - something went wrong here
        else{
            System.out.println("\nERROR: connection object is null\n\tredirecting to login...");
        }//end else
        */
        try {   //we should fix this query AND WHILE LOOP
            conn = Tables.getConnection();
            List<ServiceRequestReportEntry> apps = new ArrayList<>();
            Statement getRev = conn.createStatement();
            ResultSet rev = getRev.executeQuery(SERVICE_REQUEST_REPORT_QUERY);

            int i = 0;
            if(!rev.next()){
                messages.setText(EMPTY_QUERY_MSG);
            }//end if
            else{
                do{
                    int monthInt = rev.getInt("Res_Month");
                    if (monthInt == 8) {
                        month = "August";
                    } else if (monthInt == 9) {
                        month = "September";
                    } else if (monthInt == 10) {
                        month = "October";
                    }
                    req = rev.getString("Issue_Type");
                    day = rev.getString("resolved_time");
                    apps.add(new ServiceRequestReportEntry(month, req, day));
                    System.out.println(apps.get(i));
                    i++;
                }//end do
                while(rev.next());
            }//end else
            /*
            while(rev.next()) {
                int monthInt = rev.getInt("Res_Month");
                if (monthInt == 8) {
                    month = "August";
                } else if (monthInt == 9) {
                    month = "September";
                } else if (monthInt == 10) {
                    month = "October";
                }
                req = rev.getString("Issue_Type");
                day = rev.getString("resolved_time");
                apps.add(new ServiceRequestReportEntry(month, req, day));
                System.out.println(apps.get(i));
                i++;
            }
            */
            getRev.close();

            monthCol.setCellValueFactory(
                new PropertyValueFactory<>("month"));
            reqCol.setCellValueFactory(
                new PropertyValueFactory<>("category"));
            dayCol.setCellValueFactory(
                new PropertyValueFactory<>("num"));

            data.addAll(apps);
            table.setItems(data);

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }//end method initialize

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

}//end class ServiceRequestReportController