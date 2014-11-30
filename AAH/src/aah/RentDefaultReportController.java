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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class RentDefaultReportController implements Initializable {
    
    private List<String> months;
    private String selectedMonth; 
    private ObservableList<AptDefaults> data
            = FXCollections.observableArrayList();  
    private int apt, extra, days;
    private Connection conn;
    
    @FXML
    private TableView table = new TableView(); 
    
    @FXML
    private TableColumn aptCol;
    
    @FXML
    private TableColumn extraCol;
    
    @FXML
    private TableColumn lateCol;
    
    @FXML
    private ChoiceBox monthSel = new ChoiceBox();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Tables.getConnection();
        months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        monthSel.setItems(FXCollections.observableArrayList(months));
    }    
 
    @FXML
    private void select() {
        monthSel.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) { 
                    String c = months.get(newVal.intValue());
                    selectedMonth = c;  
                }
        });
        try {
            List<AptDefaults> report = new ArrayList<>();
            String defQ = "SELECT Apartment.Apt_No as Apartment, Amt - Rent AS Late_Fee," +
                            "(Amt - Rent)/50 AS Days_Late " +
                            "FROM Pays_Rent JOIN Apartment ON " +
                            "Apartment.Apt_No = Pays_Rent.Apt_No " +
                            "WHERE (Amt - Rent)/50 > 0 " +
                            "AND Month = '" + selectedMonth + "'" +
                            "ORDER BY Amt - Rent DESC;";
            Statement getDef = conn.createStatement();
            ResultSet defs = getDef.executeQuery(defQ);
            while (defs.next()) {  
                apt = defs.getInt("Apartment");                                  
                extra = defs.getInt("Late_Fee");   
                days = defs.getInt("Days_Late");  

                report.add(new AptDefaults(apt,extra,days));

            }          
            getDef.close();

            aptCol.setCellValueFactory(
                new PropertyValueFactory<>("apt"));
            extraCol.setCellValueFactory(
                new PropertyValueFactory<>("extra"));
            lateCol.setCellValueFactory(
                new PropertyValueFactory<>("days"));

            data.addAll(report);
            table.setItems(data);

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }
    
}
