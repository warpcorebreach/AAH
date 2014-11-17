/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class ApplicationFormController implements Initializable {
    private List<String> categories, leases;
    private String selectedCat, selectedLease;

    @FXML
    private Button submit = new Button();

    @FXML
    private TextField name = new TextField();

    @FXML
    private TextField income = new TextField();

    @FXML
    private TextField minRent = new TextField();

    @FXML
    private TextField maxRent = new TextField();

    @FXML
    private TextArea prevAddr = new TextArea();

    @FXML
    private DatePicker dob = new DatePicker();

    @FXML
    private DatePicker moveIn = new DatePicker();

    @FXML
    private ChoiceBox categorySel = new ChoiceBox();

    @FXML
    private ChoiceBox leaseSel = new ChoiceBox();

    @FXML
    private RadioButton genderMale = new RadioButton();

    @FXML
    private RadioButton genderFemale = new RadioButton();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categories = new ArrayList<>();
        leases = new ArrayList<>();

        categories.add("1bdr-1bth");
        categories.add("2bdr-1bth");
        categories.add("2bdr-2bth");
        leases.add("3 months");
        leases.add("6 months");
        leases.add("12 months");

        selectedCat = "";
        selectedLease = "";

        dob.setValue(LocalDate.now());
        moveIn.setValue(LocalDate.now());

        categorySel.setItems(FXCollections.observableArrayList(categories));
        leaseSel.setItems(FXCollections.observableArrayList(leases));

    }

    @FXML
    private void submitApp(ActionEvent event) throws IOException {
        if (name.equals("")) {
            System.out.println("Please enter a valid name.");
        } else if (selectedCat.equals("")) {
            System.out.println("Please select a category.");
        } else if (selectedLease.equals("")) {
            System.out.println("Please select a lease term.");
        } else {
            System.out.println(selectedCat);
            System.out.println(selectedLease);
        }
    }

    @FXML
    private void selectCategory() {
        categorySel.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) {
                    String s = categories.get(newVal.intValue());
                    selectedCat = s;
                }
            });
    }

    @FXML
    private void selectLeaseTerm() {
        leaseSel.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) {
                    String s = leases.get(newVal.intValue());
                    selectedLease = s;
                }
            });
    }

}
