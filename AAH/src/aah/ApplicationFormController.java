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
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        selectedCat = null;
        selectedLease = null;

        dob.setValue(LocalDate.now());
        moveIn.setValue(LocalDate.now());

        categorySel.setItems(FXCollections.observableArrayList(categories));
        leaseSel.setItems(FXCollections.observableArrayList(leases));

    }

    @FXML
    private void submitApp(ActionEvent event) throws IOException, SQLException {
        LocalDate dobChoice = dob.getValue();
        LocalDate moveInChoice = moveIn.getValue();
        String nameIn = name.getText();
        String gender = (genderMale.isSelected()) ? "M" : "F";
        int lease;
        if (selectedLease.equals("12 months")) {
            lease = Integer.parseInt(selectedLease.substring(0,2));
        } else {
            lease = Integer.parseInt(selectedLease.substring(0,1));
        }
        String addr = prevAddr.getText();
        int incomeIn = Integer.parseInt(income.getText());

        // get currently logged in user
        String curUser = Tables.getCurrentUser();

        // make sure all fields are filled out
        if (nameIn.equals("")) {
            System.out.println("Please enter a valid name.");
        } else if (dobChoice.isEqual(LocalDate.now())
                || dobChoice.isAfter(LocalDate.now())) {
            System.out.println("Please choose valid birth date.");
        } else if (income.getText().equals("")) {
            System.out.println("Please enter a valid monthly income.");
        } else if (selectedCat == null) {
            System.out.println("Please select an apartment category.");
        } else if (minRent.getText().equals("")) {
            System.out.println("Please enter a value for minimum rent.");
        } else if (maxRent.getText().equals("")) {
            System.out.println("Please enter a value for max rent.");
        } else if (Integer.parseInt(minRent.getText())
            > Integer.parseInt(maxRent.getText())) {
            System.out.println("Min rent must be less than max rent.");
        } else if (moveInChoice.isEqual(LocalDate.now())
                || moveInChoice.isBefore(LocalDate.now())) {
            System.out.println("Move in date must be after today.");
        } else if(moveInChoice.isAfter(LocalDate.now().plusMonths(2))) {
            System.out.println("Move in date must be before 2 month after today.");
        } else if (selectedLease == null) {
            System.out.println("Please select a lease term.");
        } else if (addr.equals("")) {
            System.out.println("Please enter a previous address.");
        } else {
            int minRentIn = Integer.parseInt(minRent.getText());
            int maxRentIn = Integer.parseInt(maxRent.getText());

            System.out.println("Name: " + nameIn);
            System.out.println("Date of Birth: " + dobChoice);
            System.out.println("Gender: " + gender);
            System.out.println("Monthly income: " + incomeIn);
            System.out.println("Preferred category: " + selectedCat);
            System.out.println("Min rent: " + minRentIn);
            System.out.println("Max rent: " + maxRentIn);
            System.out.println("Move in date: " + moveInChoice);
            System.out.println("Preferred lease: " + selectedLease);
            System.out.println("Previous address: " + addr);

            java.sql.Date sqlDOB = java.sql.Date.valueOf(dobChoice.toString());
            java.sql.Date sqlMoveIn = java.sql.Date.valueOf(
                    moveInChoice.toString());

            // all fields properly filled, add application to DB
            String resQ = "INSERT INTO Prospective_Resident VALUES"
                    + "('" + curUser + "', '" + nameIn + "', '" + sqlDOB
                    + "', '" + gender + "', " + lease
                    + ", '" + selectedCat + "', '" + sqlMoveIn + "', "
                    + incomeIn + ", '" + addr + "', " + minRentIn + ", "
                    + maxRentIn + ");";

            Connection conn = Tables.getConnection();
            Statement newRes = conn.createStatement();
            newRes.executeUpdate(resQ);
            newRes.close();

            System.out.println("Application submitted.");

            // load and display app under review window
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(
                    getClass().getResource("ProspResidentLogin.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

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
