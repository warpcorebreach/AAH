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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class RentDefaultReportController implements Initializable {
    
    private List<String> months;
    private String selectedMonth;
    private TableView table = new TableView();  
    private ObservableList<ObservableList> data;   
    
    @FXML
    private ChoiceBox monthSel = new ChoiceBox();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void selectMonth() {
        monthSel.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) {
                    String c = months.get(newVal.intValue());
                    selectedMonth = c;
                    try {
                        data = FXCollections.observableArrayList();
                        String defQ = "SELECT Apartment.Apt_No as Apartment, Month, Amt - Rent AS Late_Fee," +
                                            "(Amt - Rent)/50 AS Days_Late " +
                                            "FROM Pays_Rent JOIN Apartment ON " +
                                            "Apartment.Apt_No = Pays_Rent.Apt_No " +
                                            "WHERE Days_Late > 0 " +
                                            "GROUP BY Month " +
                                            "ORDER BY Late_Fee DESC;";
                        Connection conn = Tables.getConnection();
                        Statement getDef = conn.createStatement();
                        ResultSet defs = getDef.executeQuery(defQ);
                        while (defs.next()) {  
                            int apt = defs.getInt("Apartment");                                  
                            int lateFee = defs.getInt("Late_Fee");   
                            int daysLate = defs.getInt("Days_Late");  

                            ObservableList<Integer> row = FXCollections.observableArrayList();  
                            for(int i=1 ; i<=defs.getMetaData().getColumnCount(); i++){                      
                                row.add(defs.getInt(i));  
                            }                          

                            data.add(row);   
                        }
                        table.setItems(data); 
                        TableColumn aptCol = new TableColumn("Apartment");  
                        aptCol.setMinWidth(100);  

                        TableColumn extraCol = new TableColumn("Extra Amount Paid($)");  
                        extraCol.setMinWidth(100);          

                        TableColumn lateCol = new TableColumn("Defaulted By");  
                        lateCol.setMinWidth(100);        

                        table.getColumns().addAll(aptCol, extraCol, lateCol);   
                        System.out.println("Table Value::" + table); 
                    }  catch (SQLException ex) {
                        System.out.println("SQL Error: " + ex.getMessage());
                    }
                }
            });
    }
    
}
