/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Julianna
 */
public class PayInfoController implements Initializable {

    @FXML
    private Label cardName = new Label();
    
    @FXML
    private TextField cardNameText = new TextField();
    
    @FXML
    private Label cardNumber = new Label();
    
    @FXML
    private TextField cardNumberText = new TextField();
    
    @FXML
    private Label expLabel = new Label();

    @FXML
    private ChoiceBox expDate = new ChoiceBox();

    @FXML
    private Label cvv = new Label();
    
    @FXML
    private TextField cvvText = new TextField();

    @FXML
    private Button saveButton = new Button();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cardName.setText("Name on the card: ");
        cardNumber.setText("Card Number: ");
        expLabel.setText("Expiration Date: ");
        cvv.setText("CVV: ");
    }
    
}
