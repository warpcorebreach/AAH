/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Julianna
 */
public class ReminderController implements Initializable {
    
    private Connection conn;
    private String curUser;
    private int selectedApt;
    private List<Integer> aptDefaults;
    
    @FXML
    private Label dateLabel = new Label();
    
    @FXML
    private Label remLabel = new Label();
    
    @FXML
    private Button sendButton = new Button();
    
    @FXML
    private ChoiceBox apts = new ChoiceBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateLabel.setText("Date: " + LocalDate.now());
        conn = Tables.getConnection();
        curUser = Tables.getCurrentUser();
        int curMonth = LocalDate.now().getMonthValue();
        int curYear = LocalDate.now().getYear();
        
        try {
            String aptQ = "SELECT Apartment.Apt_No AS apt_no"
                            + "FROM Apartment"
                            +  "WHERE NOT EXISTS (SELECT Pays_Rent.Apt_No AS rent_no"
                            +  "FROM Pays_Rent"
                            +  "WHERE apt_no = rent_no "
                            +  "AND Month = '" + curMonth + "'"
                            +  "AND Year = '" + curYear +"')";
            Statement getApt = conn.createStatement();
            ResultSet finalApt = getApt.executeQuery(aptQ);
            while (finalApt.next()) {
                aptDefaults.add(finalApt.getInt("apt_no"));
            }
            apts.setItems(FXCollections.observableArrayList(aptDefaults));
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }

    } 
    
    @FXML
    private void selectApt() {
        apts.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) {
                    int c = aptDefaults.get(newVal.intValue());
                    selectedApt = c;
                }
            });
    }
    
    @FXML
    private void sendReminder (ActionEvent event) throws IOException, SQLException {
       String remQ = "INSERT INTO REMINDER VALUES"
                        + "('" + LocalDate.now() + "', '" + selectedApt + "', "
                        + "'" + remLabel.getText() + "')";
       Statement newRem = conn.createStatement();
       newRem.executeUpdate(remQ);
       newRem.close();

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
