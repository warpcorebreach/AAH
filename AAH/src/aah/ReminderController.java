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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    private Button backButton = new Button();

    @FXML
    private ChoiceBox apts = new ChoiceBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateLabel.setText("Date: " + LocalDate.now());
        curUser = Tables.getCurrentUser();
        String curMonth = LocalDate.now().getMonth().toString();
        int curYear = LocalDate.now().getYear();
        aptDefaults = new ArrayList<>();

        try {
           /* if (LocalDate.now().getDayOfMonth() <= 3) {
                remLabel.setText("");
            } else { */
                String aptQ = "SELECT A.Apt_No AS apt_no "
                                + "FROM Apartment A JOIN Resident R "
                                + "ON A.Apt_No = R.Apt_No "
                                + "WHERE A.Apt_No NOT IN (SELECT Apt_No FROM Pays_Rent "
                                + "WHERE Month = '" + curMonth + "' "
                                + "AND Year = " + curYear + ")";
                Connection conn = Tables.getConnection();

                Statement getApt = conn.createStatement();
                ResultSet finalApt = getApt.executeQuery(aptQ);
                while (finalApt.next()) {
                    aptDefaults.add(finalApt.getInt("Apt_No"));
                }
                getApt.close();

                apts.setItems(FXCollections.observableArrayList(aptDefaults));
         //   }
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }

    }

    @FXML
    private void sendReminder (ActionEvent event) throws IOException, SQLException {
        if(LocalDate.now().getDayOfMonth() > 3) {
       String remQ = "INSERT INTO Reminder VALUES"
                        + "('" + LocalDate.now() + "', '" + apts.getValue() + "', "
                        + "'" + remLabel.getText() + "');";
       Connection conn = Tables.getConnection();
       Statement newRem = conn.createStatement();
       newRem.executeUpdate(remQ);
       newRem.close();

       System.out.println("Reminder sent.");
        } else {
                remLabel.setText("Please wait until at least the 4th to send rent" +
                        " reminders.");
        }

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
