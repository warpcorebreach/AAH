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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class ServiceRequestReportController implements Initializable {
    
    @FXML
    private TableView table = new TableView(); 
    
    private ObservableList<ObservableList> data;   
    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            TableColumn monthCol = new TableColumn("Month");  
            monthCol.setMinWidth(100);  

            TableColumn reqCol = new TableColumn("Type of Request");  
            reqCol.setMinWidth(100);          

            TableColumn dayCol = new TableColumn("Average No of Days");  
            dayCol.setMinWidth(100);        

            table.getColumns().addAll(monthCol, reqCol, dayCol);   
            System.out.println("Table Value::" + table); 
                    
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }    
    
}
