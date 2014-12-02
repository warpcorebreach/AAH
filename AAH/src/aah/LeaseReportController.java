/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.time.Month;
import static java.time.Month.*;
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
    private TableView leaseReport = new TableView();

    @FXML
    private Button back = new Button();
    @FXML
    private TableColumn monthCol = new TableColumn();
    @FXML
    private TableColumn categoryCol = new TableColumn();
    @FXML
    private TableColumn numberCol = new TableColumn();
    @FXML
    private Label messages = new Label();
    
    private ObservableList<LeaseReportEntry> data = FXCollections.observableArrayList();
    private String month;
    private String cat;
    private String num;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {   //we should fix this query AND WHILE LOOP
            conn = Tables.getConnection();
            List<LeaseReportEntry> apps = new ArrayList<>();
            Statement getRev = conn.createStatement();
            ResultSet rev = getRev.executeQuery(LEASE_REPORT_QUERY);

            int i = 0;
            if(!rev.next()){
                messages.setText(EMPTY_QUERY_MSG);
            }//end if
            else{
                do{
                    Month m;
                    month = rev.getString("Month");
                    cat = rev.getString("Category");
                    num = rev.getString("apt_count");
                    if(month.equals("August")) {
                        m = AUGUST;
                    } else if(month.equals("September")) {
                        m = SEPTEMBER;
                    } else if(month.equals("October")) {
                        m = OCTOBER;
                    } else {
                        m = JULY;
                    }
                    apps.add(new LeaseReportEntry(m, cat, num));
                    System.out.println(apps.get(i));
                    i++;
                }//end do
                while(rev.next());
            }//end else
            /*
            while(rev.next()) {
                month = rev.getString("Month");
                cat = rev.getString("Category");
                num = rev.getString("apt_count");

                apps.add(new LeaseReportEntry(month, cat, num));
                System.out.println(apps.get(i));
                i++;
            }
            */
            getRev.close();
            monthCol.setCellValueFactory(
                new PropertyValueFactory<>("month"));
            categoryCol.setCellValueFactory(
                new PropertyValueFactory<>("category"));
            numberCol.setCellValueFactory(
                new PropertyValueFactory<>("apt_count"));
            monthCol.setSortType(TableColumn.SortType.ASCENDING);
            data.addAll(apps);
            leaseReport.setItems(data);
            leaseReport.getSortOrder().add(monthCol);
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
