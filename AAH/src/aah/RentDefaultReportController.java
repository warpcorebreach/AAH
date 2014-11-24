/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

/**
 *
 * @author Julianna
 */
public class RentDefaultReportController implements Initializable {
    private String selectedMonth;
    private List<String> months;
    
    @FXML
    private Label month = new Label();
    
    @FXML
    private ChoiceBox monthChoice = new ChoiceBox();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        month.setText("Month: ");
    }
    
   
}

