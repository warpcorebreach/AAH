
package aah;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    private int rentMonth, rentYear, apt, rentDue, selectedCard;
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
        apt = 1111; // will be retrieved from DB
        rentDue = 1234; // will be retrieved from DB

        dateLabel.setText("Date: " + LocalDate.now().toString());
        aptLabel.setText("Apartment #: " + apt);
        rentLabel.setText("Rent due: $" + rentDue);

        rentForMonth.setValue(LocalDate.now());
        rentMonth = rentForMonth.getValue().getMonthValue();
        rentYear = rentForMonth.getValue().getYear();

        cards = new ArrayList<>();
        // populate cards with the current user's available payment methods
    }

    @FXML
    private void makePayment(ActionEvent event) {

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
