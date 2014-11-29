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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class AppReviewController implements Initializable {
    
    @FXML
    private Button nextButton = new Button();
    
    @FXML
    private TableView table = new TableView(); 
    
    @FXML
    private TableColumn nameCol;
    
    @FXML
    private TableColumn dobCol;
    
    @FXML
    private TableColumn genCol;
    
    @FXML
    private TableColumn inCol;
    
    @FXML
    private TableColumn typeCol;
    
    @FXML
    private TableColumn moveCol;
    
    @FXML
    private TableColumn leCol;
    
    @FXML
    private TableColumn rejCol;
    
    @FXML
    private TableColumn checkCol;
    
    private ObservableList<ObservableList> data;  

    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        try {   //we should fix this query AND WHILE LOOP
            String revQ = "SELECT Name, DOB, Gender, Income, Category, Min_Rent, " +
                            "Max_Rent, Move_Date, Lease_Term, Username " +
                            "FROM Prospective_Resident " +
                            "WHERE Prospective_Resident.Username NOT IN (SELECT Username FROM " +
                            "	Resident) " +
                            "AND ((Income/3) >= SELECT MAX(Rent) FROM Apartment " +
                            "WHERE Apartment.Category = Prospective_Resident.Category) " +
                            "AND (Move_Date >= SELECT MIN(Available_On) FROM Apartment WHERE " +
                            "Apartment.Category = Prospective_Resident.Category);";
            Connection conn = Tables.getConnection();
            Statement getRev = conn.createStatement();
            ResultSet rev = getRev.executeQuery(revQ);
            while (rev.next()) {
                String name = rev.getString("Name");
                Date dob = rev.getDate("DOB");
                String Gen = rev.getString("Gender");
                int income = rev.getInt("Income");
                String cat = rev.getString("Category");
                int min = rev.getInt("Min_Rent");
                int max = rev.getInt("Max_Rent");
                Date move = rev.getDate("Move_Date");
                String term = rev.getString("Lease_Term");
                String user = rev.getString("Username");
                CheckBox check = new CheckBox();
                
                ObservableList<String> row = FXCollections.observableArrayList();  
                for(int i=1 ; i<=rev.getMetaData().getColumnCount(); i++){                      
                    row.add(rev.getString(i));  
                }
                data.add(row); 
            }
            table.setItems(data); 
            nameCol = new TableColumn("Name");  
            nameCol.setMinWidth(100);  

            dobCol = new TableColumn("Date of Birth");  
            dobCol.setMinWidth(100);          

            genCol = new TableColumn("Gender");  
            genCol.setMinWidth(100);  
            
            inCol = new TableColumn("Monthly Income($)");  
            inCol.setMinWidth(100);  
            
            typeCol = new TableColumn("Type of Apartment Requested");  
            typeCol.setMinWidth(100);  

            moveCol = new TableColumn("Preferred Move-in Date");  
            moveCol.setMinWidth(100);          

            leCol = new TableColumn("Lease Term");  
            leCol.setMinWidth(100);  
            
            rejCol = new TableColumn("Reject/Accept");  
            rejCol.setMinWidth(100); 
            
            checkCol = new TableColumn("");  
            checkCol.setMinWidth(100); 

            table.getColumns().addAll(nameCol, dobCol, genCol, inCol, typeCol, 
                    moveCol, leCol, rejCol, checkCol);
         
            System.out.println("Table Value::" + table); 
            
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }
    
    @FXML
    private void next(ActionEvent event)throws IOException, SQLException {
        
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root;
        root = FXMLLoader.load(
                getClass().getResource("AptAllotment.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
