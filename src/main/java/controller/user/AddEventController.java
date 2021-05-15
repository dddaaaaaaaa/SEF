package controller.user;

import domain.PersonalEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddEventController extends PersonalEventsListController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private TextField EventNameTextField, ObservationsTextField, HostTextField, LocationTextField, HourTextField, MinuteTextField;
    @FXML
    private DatePicker DateField;
    @FXML
    private Label MandatoryLabelField;
    @FXML
    private Button AddEventButton, CancelEventAdditionButton;
    @FXML
    private javafx.scene.control.TableView<PersonalEvent> TableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src\\main\\resources\\images\\sigla\\event.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    public void addEventButtonOnAction(javafx.event.ActionEvent actionEvent) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        LocalDate localDate = DateField.getValue();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HourTextField.getText()));
        cal.set(Calendar.MINUTE, Integer.parseInt(MinuteTextField.getText()));
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();

        PersonalEvent personalEvent = new PersonalEvent(date, EventNameTextField.getText(), ObservationsTextField.getText(), HostTextField.getText(), LocationTextField.getText());

        if (!(EventNameTextField.getText().isBlank()) && !(DateField.getValue() == null) && !(ObservationsTextField).getText().isBlank()) {
            try {
                getPersonalEvent(personalEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            MandatoryLabelField.setText("All field are mandatory!");
        }
    }

    public void getPersonalEvent(PersonalEvent personalEvent) throws ParseException {
        // PersonalEventsListController controller = new PersonalEventsListController();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = DateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        events.add(personalEvent);
        Stage stage = (Stage) AddEventButton.getScene().getWindow();
        stage.close();

    }

    public void cancelEventAdditionButtonOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) CancelEventAdditionButton.getScene().getWindow();
        stage.close();
    }
}
