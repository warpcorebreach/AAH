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
    private boolean valid;
    private Connection conn;
    private String curUser;

    @FXML
    private Button payButton = new Button();

    @FXML
    private Button calcButton = new Button();

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
        try {
            conn = Tables.getConnection();
            curUser = Tables.getCurrentUser();
            valid = true;
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
            for (int x = 2014; x < 2017; x++) {
                years.add(x);
            }
            rentYear.setItems(FXCollections.observableArrayList(years));

            Statement aptRent = conn.createStatement();
            ResultSet ar = aptRent.executeQuery("SELECT A.Apt_No, Rent " +
                    "FROM Apartment A JOIN Resident R " +
                    "ON A.Apt_No = R.Apt_No " +
                    "WHERE R.Username = '" + curUser + "'");
            if (ar.next()) {
                aptLabel.setText("Apartment No.: "+ar.getInt("Apt_No"));
                rentLabel.setText("Base Monthly Rent: $" + ar.getInt("Rent"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @FXML
    private void selectMonth(ActionEvent event) throws IOException {
        if (rentForMonth.getValue() == null || rentYear.getValue() == null) {
            System.out.println("Error: Please fill out all fields.");
            return;
        }
        int month = rentForMonth.getSelectionModel().getSelectedIndex()+1;
        int year = (int) rentYear.getValue();

        try {
            // get current user's data
            int baseRent, mDay, mMonth, mYear;
            String aptRent = "SELECT A.Apt_No, Rent, "
                    + "EXTRACT(DAY FROM(Move_Date)) AS day, "
                    + "EXTRACT(MONTH FROM(Move_Date)) AS month, "
                    + "EXTRACT(YEAR FROM(Move_Date)) AS year "
                    + "FROM Apartment A JOIN Resident R "
                    + "ON A.Apt_No = R.Apt_No "
                    + "JOIN Prospective_Resident P "
                    + "ON R.Username = P.Username "
                    + "WHERE R.Username = '" + curUser + "'";
            Statement getAptRent = conn.createStatement();
            ResultSet finalAptRent = getAptRent.executeQuery(aptRent);
            finalAptRent.next();

            apt = finalAptRent.getInt("Apt_No");
            aptLabel.setText("Apartment No.: " + apt);
            baseRent = finalAptRent.getInt("Rent");
            mDay = finalAptRent.getInt("day");
            mMonth = finalAptRent.getInt("month");
            mYear = finalAptRent.getInt("year");

            getAptRent.close();

            // determine if rent should be prorated
            String toProrate = "SELECT Count(*) AS count "
                    + "FROM Prospective_Resident P JOIN Resident R "
                    + "ON P.Username = R.Username "
                    + "WHERE P.Username = '" + curUser + "' "
                    + "AND EXTRACT(DAY FROM Move_Date) > 7 "
                    + "AND EXTRACT(MONTH FROM Move_Date) = " + month;
            Statement pro = conn.createStatement();
            ResultSet prorate = pro.executeQuery(toProrate);

            int count = 0;
            if (prorate.next()) {
                count = prorate.getInt("count");
            }
            pro.close();
            if (count == 1) {
                //prorate rent
                String proAmt = "SELECT (EXTRACT(DAY FROM CURDATE())"
                        + "- EXTRACT(DAY FROM Move_Date))"
                        + "*(Rent / LAST_DAY(CURDATE())) AS prorated_rent "
                        + "FROM Prospective_Resident P JOIN Resident R "
                        + "ON P.Username = R.Username "
                        + "JOIN Apartment A "
                        + "ON R.Apt_No = A.Apt_No "
                        + "WHERE P.Username = '" + curUser + "'";

                String proAmt1 = "SELECT Rent FROM " +
                        "Apartment A JOIN Resident R " +
                        "ON A.Apt_No = R.Apt_No " +
                        "WHERE R.Username = '" + curUser + "'";

                Statement proCalc = conn.createStatement();
                ResultSet finalPro = proCalc.executeQuery(proAmt1);
                finalPro.next();
                rentDue = (LocalDate.now().lengthOfMonth() - mDay)*(baseRent/LocalDate.now().lengthOfMonth());
                proCalc.close();
            } else {
                // handle late rent
                String delayed = "SELECT A.Apt_No, " + //now only calcs extra default amt
                        "50*(" + LocalDate.now().getDayOfMonth() + "-3) AS extra_rent "
                        + "FROM Apartment A JOIN Resident R "
                        + "ON A.Apt_No = R.Apt_No "
                        + "WHERE Username = '" + curUser + "'";
                Statement getDelay = conn.createStatement();
                ResultSet extra = getDelay.executeQuery(delayed);
                if (extra.next()) {
                    if (extra.getInt("extra_rent") > 0) {
                        rentDue = baseRent + extra.getInt("extra_rent");
                        System.out.println("rent defaulted: " + rentDue);
                    } else {
                        rentDue = baseRent;
                        System.out.println("final rent: " + rentDue);
                    }
                }
                getDelay.close();
            }

            rentLabel.setText("Rent Due: $" + rentDue);

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
        if (rentForMonth.getValue() == null || rentYear.getValue() == null
                || cardChoice.getValue() == null) {
            System.out.println("Error: Please ensure all fields are filled.");
            return;
        }
        if (valid) {
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
                    System.out.println("You have already paid your rent this month.");
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
        } else {
            System.out.println("Invalid operation.");
        }

    }

}
