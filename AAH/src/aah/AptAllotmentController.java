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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class AptAllotmentController implements Initializable {
    
    @FXML
    private Button allotButton = new Button();
    
    @FXML
    private Label nameLabel = new Label();
    
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
            String allQ = "SELECT (Apt_No, APARTMENT.Category, Rent, Sq_Ft, Available_On, Name) " +
                            "FROM APARTMENT JOIN PROSPECTIVE_RESIDENT " +
                            "	ON APARTMENT.Category = PROSPECTIVE_RESIDENT.Category " +
                            "WHERE (Rent >= Min_Rent AND Rent <= Max_Rent AND Move_Date >= Available_On " +
                            "	AND Pref_Lease = Lease_Term AND Username = $username) " +
                            "ORDER BY Rent DESC;";
            Connection conn = Tables.getConnection();
            Statement getAll = conn.createStatement();
            ResultSet allot = getAll.executeQuery(allQ);
            while (allot.next()) {
                int apt = allot.getInt("Apt_No");
                String cat = allot.getString("Category");
                int rent = allot.getInt("Rent");
                int feet = allot.getInt("Sq_Ft");
                Date avail = allot.getDate("Available_On");
                String name = allot.getString("Name");
                CheckBox check = new CheckBox();
                
                ObservableList<String> row = FXCollections.observableArrayList();  
                for(int i=1 ; i<=allot.getMetaData().getColumnCount(); i++){                      
                    row.add(allot.getString(i));  
                }
                data.add(row); 
            }
            table.setItems(data); 
            TableColumn aptCol = new TableColumn("Apartment No");  
            aptCol.setMinWidth(100);  

            TableColumn catCol = new TableColumn("Category");  
            catCol.setMinWidth(200);          

            TableColumn rentCol = new TableColumn("Monthly Rent($)");  
            rentCol.setMinWidth(100);  
            
            TableColumn feetCol = new TableColumn("Sq Ft.");  
            feetCol.setMinWidth(100); 
            
            TableColumn dateCol = new TableColumn("Available From");  
            dateCol.setMinWidth(200); 
            
            TableColumn checkCol = new TableColumn("");  
            checkCol.setMinWidth(100); 

            table.getColumns().addAll(aptCol, catCol, rentCol, feetCol, dateCol, checkCol);  
            
            try {
                ActionEvent event = null;
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                Parent root;
                root = FXMLLoader.load(
                        getClass().getResource("AptAllotment.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.out.println("IO Error: " + ex.getMessage());
            }
            System.out.println("Table Value::" + table); 
            
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }  
    
    @FXML
    private void assign(ActionEvent event)throws IOException, SQLException {
        String assQ = "INSERT INTO Resident VALUES ($username, $apt_no);";
        Connection conn = Tables.getConnection();
        Statement assRes = conn.createStatement();
        assRes.executeUpdate(assQ);
        assRes.close(); 
        
        String upQ = "UPDATE APARTMENT " +
                        "SET Available_On = " +
                        "DATE_ADD((SELECT Available_On " +
                        " FROM APARTMENT A JOIN RESIDENT R " +
                        " ON A.Apt_No = R.Apt_No " +
                        " WHERE Username = $username), " +
                        "INTERVAL (SELECT Pref_Lease " +
                        " FROM APARTMENT A JOIN RESIDENT R " +
                        " ON A.Apt_No = R.Apt_No\n" +
                        " WHERE Username = $username) MONTH) " +
                        "WHERE Apt_No = $apt_no;";
        Statement upRes = conn.createStatement();
        upRes.executeUpdate(upQ);
        upRes.close();
        conn.close();
    }
    
}
