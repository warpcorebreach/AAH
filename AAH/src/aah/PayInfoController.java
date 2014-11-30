/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Julianna
 */
public class PayInfoController implements Initializable {
    
    private Connection conn;
    private String curUser;
    private List<Integer> cards;
    private List<Date> dates;
    private java.sql.Date selectedDate;
    private int selectedCard;
    
    @FXML
    private TextField cardNameText = new TextField();
    
    @FXML
    private TextField cardNumberText = new TextField();

    @FXML
    private ChoiceBox expDate = new ChoiceBox();
    
    @FXML
    private ChoiceBox cardBox = new ChoiceBox();
    
    @FXML
    private TextField cvvText = new TextField();

    @FXML
    private Button saveButton = new Button();
    
    /**
     * Initializes the controller class.
     */
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        conn = Tables.getConnection();
        curUser = Tables.getCurrentUser();
        expDate.setValue("Choose a date");
        
        String aStr;
        String bStr;
        dates = new ArrayList<>();
        
        for (int y = 14; y < 25; y++) {
            aStr = "" + y;
            for (int x = 0; x < 12; x++) {
                bStr = x + "/" + aStr;
                String DateStr="20" + y + "-" + x + "-01";
                try {
                    Date d = new SimpleDateFormat("yyyy-MM-dd").parse(DateStr); 
                    java.sql.Date date = new java.sql.Date(d.getTime());
                    dates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        
        expDate.setItems(FXCollections.observableArrayList(dates));
        
        cards = new ArrayList<>();
        try {
            String getQ = "SELECT Card_No " +
                            "FROM Payment_Info " +
                            "WHERE Username = '" + curUser + "';";
            Statement getCard = conn.createStatement();
            ResultSet get = getCard.executeQuery(getQ);
            while (get.next()) {
                cards.add(get.getInt("Card_No"));
            }
        } catch(SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
        cardBox.setItems(FXCollections.observableArrayList(cards));
    }
    
    @FXML
    private void saveCard(ActionEvent event)throws IOException, SQLException {
        String cardNo = cardNumberText.getText();
        long cardNoInt = Integer.parseInt(cardNo);
        String cvv = cvvText.getText();
        int cvvInt = Integer.parseInt(cvv);
        String cardName = cardNameText.getText(); 
        
        String addQ = "INSERT INTO Payment_Info VALUES"
                        + "('" + cardNoInt + "', '" + cvvInt + "', '" + cardName
                        + "', '" + expDate.getValue() + "', '" + curUser + "');";

        Statement newCard = conn.createStatement();
        newCard.executeUpdate(addQ);
        newCard.close();
        
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(
                    getClass().getResource("CardSave.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        System.out.println("Card added.");
    }
    
    @FXML
    private void deleteCard(ActionEvent event)throws IOException, SQLException {
        String delQ = "DELETE FROM Payment_Info " 
                        + "WHERE Card_No = '" + cardBox.getValue() + "';";
        Statement delCard = conn.createStatement();
        delCard.executeUpdate(delQ);
        delCard.close();
        
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(
                    getClass().getResource("CardDelete.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        System.out.println("Card deleted.");
    }
}
