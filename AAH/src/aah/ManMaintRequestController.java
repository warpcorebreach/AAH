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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    private Button back  = new Button();
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
    
    private ObservableList<MaintRequestEntry> data2
             = FXCollections.observableArrayList();
    private Date requestDate;
    private int aptno;
    private String issue;
    private Date resolvedDate;
    private MaintRequestEntry selected;
    private List<MaintRequestEntry> entrys = new ArrayList<>();
    private List<MaintRequestEntry> entrys2 = new ArrayList<>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {            
            Connection conn = Tables.getConnection();

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
                entrys.add(new MaintRequestEntry(requestDate,aptno,issue, null));
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
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
        try {                

            String resQ = "SELECT Request_Date, Apt_No, Issue_Type, Resolved_Date " +
                            "FROM Maintenance_Request " +
                            "WHERE Resolved_Date IS NOT NULL " +
                            "ORDER BY Request_Date DESC;";
            Connection conn = Tables.getConnection();
            Statement getRes = conn.createStatement();
            ResultSet res = getRes.executeQuery(resQ);
            while (res.next()) {
                requestDate = res.getDate("Request_Date");
                aptno = res.getInt("Apt_No");
                issue = res.getString("Issue_Type");
                resolvedDate = res.getDate("Resolved_Date");

                entrys2.add(new MaintRequestEntry(requestDate, aptno, issue, resolvedDate));
            }
            getRes.close();
                dor2.setCellValueFactory(
                new PropertyValueFactory<>("requestDate"));
                aptCol2.setCellValueFactory(
                new PropertyValueFactory<>("aptno"));
                desCol2.setCellValueFactory(
                new PropertyValueFactory<>("issue"));
                resolved.setCellValueFactory(
                new PropertyValueFactory<>("resolvedDate"));
                data2.addAll(entrys2);
                table2.setItems(data2);
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        } 
    } 
    
    @FXML
    private void resolve(ActionEvent event)throws IOException, SQLException {
        if(selected != null) {
            System.out.println(selected.getAptno() + selected.getIssue() + selected.getRequestDate());
            System.out.println(LocalDate.now());
        String updateQu = "UPDATE Maintenance_Request " +
                           "SET Resolved_Date = '"+ LocalDate.now() +"' " +
                           "WHERE Apt_No = '" + selected.getAptno() + "' AND Request_Date = '" + selected.getRequestDate() +"' AND " +
                           "Issue_Type = '"+ selected.getIssue() + "';";
        Connection conn = Tables.getConnection();
        Statement newRes = conn.createStatement();
        newRes.executeUpdate(updateQu);
        newRes.close();
        data.remove(selected);
        selected.setResolvedDate(new Date(114,LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth()));
        data2.add(selected);
        }
    }
    @FXML
    private void select() {
        selected = (MaintRequestEntry) table.getSelectionModel().getSelectedItem();
    }
    
    @FXML
    private void loadHomepage(ActionEvent event) throws IOException, SQLException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(
                    getClass().getResource("MgrHomepage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
   }
}
