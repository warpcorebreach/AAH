/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class ManMaintRequestController implements Initializable {

    @FXML
    private TableView table = new TableView(); 
    
    @FXML
    private TableView table2 = new TableView();
    
    @FXML
    private Button resolveButton = new Button();
    @FXML
    private TableColumn dor = new TableColumn();
    @FXML
    private TableColumn aptCol = new TableColumn();
    @FXML
    private TableColumn desCol = new TableColumn();
    @FXML
    private TableColumn dor2 = new TableColumn();
    @FXML
    private TableColumn aptCol2 = new TableColumn();
    @FXML
    private TableColumn desCol2 = new TableColumn();
    @FXML
    private TableColumn resolved = new TableColumn();
    
    private ObservableList<MaintRequestEntry> data
            = FXCollections.observableArrayList();
    
    private ObservableList<ObservableList> data2;
    private Date requestDate;
    private int aptno;
    private String issue;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        try {            
            Connection conn = Tables.getConnection();
            List<MaintRequestEntry> entrys = new ArrayList<>();
            String notResQ = "SELECT Request_Date, Apt_No, Issue_Type " +
                                "FROM Maintenance_Request " +
                                "WHERE Resolved_Date IS NULL "  +
                                "ORDER BY Request_Date ASC;";

            Statement getUnRes = conn.createStatement();
            ResultSet notRes = getUnRes.executeQuery(notResQ);
            while (notRes.next()) {
                requestDate = notRes.getDate("Request_Date");
                aptno = notRes.getInt("Apt_No");
                issue = notRes.getString("Issue_Type");
                CheckBox check = new CheckBox();
                System.out.println(aptno + " " + requestDate + " " + issue);
                entrys.add(new MaintRequestEntry(requestDate,aptno,issue,null));
              /*  ObservableList<String> row = FXCollections.observableArrayList();  
                for(int i=1 ; i<=notRes.getMetaData().getColumnCount(); i++){                      
                    row.add(notRes.getString(i));  
                }
                data.add(row); */
            }
            getUnRes.close();
                dor.setCellValueFactory(
                new PropertyValueFactory<>("requestDate"));
                aptCol.setCellValueFactory(
                new PropertyValueFactory<>("aptno"));
                desCol.setCellValueFactory(
                new PropertyValueFactory<>("issue"));
                data.addAll(entrys);
            table.setItems(data);
     /*       TableColumn notReqCol = new TableColumn("Date of Request");  
            notReqCol.setMinWidth(100);  

            TableColumn aptCol = new TableColumn("Apt No");  
            aptCol.setMinWidth(100);          

            TableColumn issCol = new TableColumn("Description of Issue");  
            issCol.setMinWidth(100); 
            
            TableColumn boxes = new TableColumn("");  
            boxes.setMinWidth(100); 

            table.getColumns().addAll(notReqCol, aptCol, boxes);   */ 
            System.out.println("Table Value::" + table2); 
            
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
     /*   data2 = FXCollections.observableArrayList();
        try {
            String resQ = "SELECT Request_Date, Apt_No, Issue_Type, Resolved_Date " +
                            "FROM Maintenance_Request " +
                            "WHERE Resolved_Date IS NOT NULL " +
                            "ORDER BY Request_Date DESC;";
            Connection conn = Tables.getConnection();
            Statement getRes = conn.createStatement();
            ResultSet res = getRes.executeQuery(resQ);
            while (res.next()) {
                Date start = res.getDate("Request_Date");
                int apt = res.getInt("Apt_No");
                String issue = res.getString("Issue_Type");
                Date end = res.getDate("Resolved_Date");
                
                ObservableList<String> row2 = FXCollections.observableArrayList();  
                for(int i=1 ; i<=res.getMetaData().getColumnCount(); i++){                      
                    row2.add(res.getString(i));  
                }
                data2.add(row2); 
            }
            table2.setItems(data2); 
            TableColumn reqCol = new TableColumn("Date of Request");  
            reqCol.setMinWidth(100);  

            TableColumn apt2Col = new TableColumn("Apt No");  
            apt2Col.setMinWidth(100);          

            TableColumn iss2Col = new TableColumn("Description of Issue");  
            iss2Col.setMinWidth(100);  
            
            TableColumn resCol = new TableColumn("Issue Resolved On");  
            resCol.setMinWidth(100);      

            table.getColumns().addAll(reqCol, apt2Col, iss2Col, resCol);   
            System.out.println("Table Value::" + table2); 
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        } */
    } 
    
    @FXML
    private void resolve(ActionEvent event)throws IOException, SQLException {
        String updateQ = "UPDATE Maintenance_Request " +
                            "SET Date_Resolved = $date_resolved " +
                            "WHERE Apt_No = $apt AND Request_Date = $date AND " +
                            "Issue_Type = $issue;";
        Connection conn = Tables.getConnection();
        Statement newRes = conn.createStatement();
        newRes.executeUpdate(updateQ);
        newRes.close();
    }
    
}
