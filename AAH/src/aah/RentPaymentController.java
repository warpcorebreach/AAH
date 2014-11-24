
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Justin
 */
public class RentPaymentController implements Initializable {
    private int rentYear, apt, rentDue, selectedCard;
    private Connection conn;
    private String rentMonth;
    private String[] months = {"January", "February", "March", "April", "May",
      "June", "July", "August", "September", "October", "November", "December"};
    private List<Integer> cards;

    @FXML
    private Button payButton = new Button();

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
        try {
            apt = 1111; // will be retrieved from DB
            rentDue = 1234; // will be retrieved from DB
            conn = Tables.getConnection();

            String curUser = Tables.getCurrentUser();
            int baseRent = 0;

            String aptRent = "SELECT Apt_No, Rent" +
                    "FROM Apartment A JOIN Resident R" +
                    "ON A.Apt_No = R.Apt_No" +
                    "WHERE Username = '" + curUser + "'";
            Statement getAptRent = conn.createStatement();
            ResultSet finalAptRent = getAptRent.executeQuery(aptRent);
            if (finalAptRent.next()) {
                apt = finalAptRent.getInt("Apt_No");
                baseRent = finalAptRent.getInt("Rent");
            }

            String toProrate = "SELECT EXTRACT(DAY FROM Move_Date) AS Day," +
                    "EXTRACT(MONTH FROM Move_Date) AS Month," +
                    "EXTRACT(MONTH FROM CURDATE()) AS cur_month, Count(*) AS count" +
                    "FROM Prospective_Resident P JOIN Resident R" +
                    "ON P.Username = R.Username" +
                    "WHERE P.Username = '" + curUser + "'" +
                    "AND Day > 7" +
                    "AND Month = cur_month";
            Statement pro = conn.createStatement();
            ResultSet prorate = pro.executeQuery(toProrate);
            prorate.next();
            if (prorate.getInt("count") == 1) {
                String proAmt = "SELECT (EXTRACT(DAY FROM CURDATE())" +
                        "- EXTRACT(DAY FROM Move_Date))" +
                        "*(Rent / LAST_DAY(CURDATE())) AS prorated_rent" +
                        "FROM rospective_Resident P JOIN Resident R" +
                        "ON P.Username = R.Username" +
                        "JOIN APARTMENT A" +
                        "ON R.Apt_No = A.Apt_No" +
                        "WHERE P.Username = '" + curUser + "'";
                Statement proCalc = conn.createStatement();
                ResultSet finalPro = proCalc.executeQuery(proAmt);
                finalPro.next();
                baseRent = finalPro.getInt("prorated_rent");
            }

            String delayed = "SELECT Apt_No, " + //now only calcs extra default amt
                    "50*(EXTRACT(DAY FROM CURDATE())-3) AS extra_rent" +
                    "FROM Apartment A JOIN Resident R" +
                    "	ON A.Apt_No = R.Apt_No" +
                    "WHERE Username = '" + curUser + "'";
            Statement getDelay = conn.createStatement();
            ResultSet extra = getDelay.executeQuery(delayed);
            extra.next();
            rentDue = baseRent + extra.getInt("extraRent");

            int curMonth;

            dateLabel.setText("Date: " + LocalDate.now().toString());
            aptLabel.setText("Apartment #: " + apt);
            rentLabel.setText("Rent due: $" + rentDue);

            rentForMonth.setValue(LocalDate.now());

            curMonth = rentForMonth.getValue().getMonthValue();
            rentMonth = months[curMonth - 1];
            rentYear = rentForMonth.getValue().getYear();
            cards = new ArrayList<>();
            // populate cards with the current user's available payment methods

            String cardPots = "SELECT Card_No" +
                    "FROM Payment_Info" +
                    "WHERE Username = '" + curUser + "'";
            Statement getCard = conn.createStatement();
            ResultSet cardNos = getCard.executeQuery(cardPots);
            while (cardNos.next()) {
                cards.add(cardNos.getInt("Card_No"));
            }
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }

    @FXML
    private void makePayment(ActionEvent event)throws IOException, SQLException {

        String resQ = "INSERT INTO Pays_Rent VALUES"
                    + "('" + selectedCard + "', '" + rentMonth + "', '" + rentYear
                    + "', '" + apt + "', " + rentDue
                    + ", '" + rentForMonth + ")";

        Statement newRes = conn.createStatement();
        newRes.executeUpdate(resQ);
        newRes.close();

        System.out.println("Payment submitted.");
    }

    @FXML
    private void selectCard() {
        cardChoice.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue v, Number val, Number newVal) {
                    int c = cards.get(newVal.intValue());
                    selectedCard = c;
                }
            });
    }

}
