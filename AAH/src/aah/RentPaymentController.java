/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class RentPaymentController implements Initializable {
    private int rentMonth, rentYear, apt, rentDue;

    @FXML
    private Label dateLabel = new Label();

    @FXML
    private Label aptLabel = new Label();

    @FXML
    private Label rentLabel = new Label();

    @FXML
    private DatePicker rentForMonth = new DatePicker();

    @FXML
    private ChoiceBox cardChoice = new ChoiceBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apt = 1111; // will be retrieved from DB
        rentDue = 1234; // will be retrieved from DB

        dateLabel.setText("Date: " + LocalDate.now().toString());
        aptLabel.setText("Apartment #: " + apt);
        rentLabel.setText("Rent due: $" + rentDue);
        
        rentForMonth.setValue(LocalDate.now());
        rentMonth = rentForMonth.getValue().getMonthValue();
        rentYear = rentForMonth.getValue().getYear();
    }

}
