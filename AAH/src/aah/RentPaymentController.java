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
 * @author Justin
 */
public class RentPaymentController implements Initializable {

    private int apt, rentDue;
    private List<String> months;
    private List<Integer> years;
    private List<String> cards;

    @FXML
    private Button payButton = new Button();

    @FXML
    private Label dateLabel = new Label();

    @FXML
    private Label aptLabel = new Label();

    @FXML
    private Label rentLabel = new Label();

    @FXML
    private ChoiceBox rentForMonth = new ChoiceBox();

    @FXML
    private ChoiceBox rentYear = new ChoiceBox();

    @FXML
    private ChoiceBox cardChoice = new ChoiceBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateLabel.setText("Date: " + LocalDate.now());
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
        rentForMonth.setItems(FXCollections.observableArrayList(months));
        years = new ArrayList<>();
        for (int x = 2010; x < 2021; x++) {
            years.add(x);
        }
        rentYear.setItems(FXCollections.observableArrayList(years));
        try {

            String curUser = Tables.getCurrentUser();
            int baseRent = 0;
            String aptRent = "SELECT A.Apt_No, Rent "
                    + "FROM Apartment A JOIN Resident R "
                    + "ON A.Apt_No = R.Apt_No "
                    + "WHERE Username = '" + curUser + "';";
            Connection conn = Tables.getConnection();
            Statement getAptRent = conn.createStatement();
            ResultSet finalAptRent = getAptRent.executeQuery(aptRent);
            if (finalAptRent.next()) {
                apt = finalAptRent.getInt("Apt_No");
                aptLabel.setText("Apartment No.: " + apt);
                baseRent = finalAptRent.getInt("Rent");
            }

            String toProrate = "SELECT Count(*) AS count "
                    + "FROM Prospective_Resident P JOIN Resident R "
                    + "ON P.Username = R.Username "
                    + "WHERE P.Username = '" + curUser + "' "
                    + "AND EXTRACT(DAY FROM Move_Date) > 7 "
                    + "AND EXTRACT(MONTH FROM Move_Date) = EXTRACT(MONTH FROM CURDATE());";
            Statement pro = conn.createStatement();
            ResultSet prorate = pro.executeQuery(toProrate);
            prorate.next();

            if (prorate.getInt("count") == 1) {
                String proAmt = "SELECT (EXTRACT(DAY FROM CURDATE())"
                        + "- EXTRACT(DAY FROM Move_Date))"
                        + "*(Rent / LAST_DAY(CURDATE())) AS prorated_rent "
                        + "FROM Prospective_Resident P JOIN Resident R "
                        + "ON P.Username = R.Username "
                        + "JOIN APARTMENT A "
                        + "ON R.Apt_No = A.Apt_No "
                        + "WHERE P.Username = '" + curUser + "';";
                Statement proCalc = conn.createStatement();
                ResultSet finalPro = proCalc.executeQuery(proAmt);
                if (finalPro.next()) {
                    baseRent = finalPro.getInt("prorated_rent");
                }
            }

            String delayed = "SELECT A.Apt_No, " + //now only calcs extra default amt
                    "50*(EXTRACT(DAY FROM CURDATE())-3) AS extra_rent "
                    + "FROM Apartment A JOIN Resident R "
                    + "ON A.Apt_No = R.Apt_No "
                    + "WHERE Username = '" + curUser + "';";
            Statement getDelay = conn.createStatement();
            ResultSet extra = getDelay.executeQuery(delayed);
            if (extra.next()) {
                if (extra.getInt("extra_rent") > 0) {
                    rentDue = baseRent + extra.getInt("extra_rent");
                } else {
                    rentDue = baseRent;
                }
            }
            int monthValue = LocalDate.now().getMonthValue();

            String actualMonth = months.get(monthValue -1);
            String monthGet = (String) rentForMonth.getValue();
            if (actualMonth.equals(monthGet)) {
                rentDue = baseRent;
            }

            rentLabel.setText("Rent due: $" + rentDue);

            cards = new ArrayList<>();
            // populate cards with the current user's available payment methods

            String cardPots = "SELECT Card_No "
                    + "FROM Payment_Info "
                    + "WHERE Username = '" + curUser + "';";
            Statement getCard = conn.createStatement();
            ResultSet cardNos = getCard.executeQuery(cardPots);
            while (cardNos.next()) {
                cards.add(cardNos.getString("Card_No"));
            }
            cardChoice.setItems(FXCollections.observableArrayList(cards));
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void makePayment(ActionEvent event) throws IOException, SQLException {

        Parent root = null;

        String payQ = "SELECT Month, Year "
                + "FROM Pays_Rent "
                + "WHERE Apt_No = '" + apt + "';";
        Connection conn = Tables.getConnection();
        Statement getPay = conn.createStatement();
        ResultSet payRes = getPay.executeQuery(payQ);
        while (payRes.next()) {

            if (((rentForMonth.getValue()).equals(payRes.getString("Month")))
                    && ((int) rentYear.getValue() == payRes.getInt("Year"))) {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                root = FXMLLoader.load(
                        getClass().getResource("PayDone.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
        if (root == null) {
            String resQ = "INSERT INTO Pays_Rent VALUES"
                    + "('" + cardChoice.getValue() + "', '" + rentForMonth.getValue()
                    + "', '" + rentYear.getValue()
                    + "', '" + apt + "', " + rentDue
                    + ", '" + LocalDate.now() + "');";
            Statement newRes = conn.createStatement();
            newRes.executeUpdate(resQ);
            newRes.close();

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            root = FXMLLoader.load(
                    getClass().getResource("RentReceived.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("Payment submitted.");
        }

    }

}
