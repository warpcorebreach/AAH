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
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class ServiceRequestReportController implements Initializable {
    
    //DEBUG mode- switch to true to get debug prints
    private final boolean DEBUG = true;
    
    @FXML
    private TableView table = new TableView(); 
    
    @FXML
    private TableColumn monthCol;
    
    @FXML
    private TableColumn reqCol;
    
    @FXML
    private TableColumn dayCol;
    
    private ObservableList<ObservableList> data;
    
    @FXML
    private final Button back = new Button();
    
    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        data = FXCollections.observableArrayList();
        try {
            String servQ = "SELECT EXTRACT(MONTH FROM Request_Date) AS Res_Month, " +
                                "Issue_Type, AVG(DATE_DIFF(Resolved_Date, Request_Date)+1) " +
                                "AS resolved_time " +
                                "FROM Maintenance_Request " +
                                "WHERE Resolved_Date IS NOT NULL AND Res_Month >= 8 AND Res_Month <= 10 " +
                                "GROUP BY DATE_FORMAT(Res_Month, %M), Issue_Type;";
            Connection conn = Tables.getConnection();
            Statement getServ = conn.createStatement();
            ResultSet servs = getServ.executeQuery(servQ);
            while (servs.next()) {
                String monthInt = servs.getString("Res_Month");
                String issue = servs.getString("Issue_Type");
                int time = servs.getInt("resolved_time");
                
                ObservableList<String> row = FXCollections.observableArrayList();  
                for(int i=1 ; i<=servs.getMetaData().getColumnCount(); i++){                      
                    row.add(servs.getString(i));  
                }
                data.add(row);   
            }
            table.setItems(data); 
            monthCol = new TableColumn("Month");  
            monthCol.setMinWidth(100);  

            reqCol = new TableColumn("Type of Request");  
            reqCol.setMinWidth(100);          

            dayCol = new TableColumn("Average No of Days");  
            dayCol.setMinWidth(100);        

            table.getColumns().addAll(monthCol, reqCol, dayCol);   
            System.out.println("Table Value::" + table); 
                    
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
        */
    }    
 
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
    
}
