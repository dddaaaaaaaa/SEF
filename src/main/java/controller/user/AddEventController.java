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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddEventController extends PersonalEventsListController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private TextField EventNameTextField, ObservationsTextField;
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

        PersonalEvent personalEvent = new PersonalEvent(formatter.parse("2021-02-02"),EventNameTextField.getText(),ObservationsTextField.getText());

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
        PersonalEventsListController controller = new PersonalEventsListController();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = DateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("Date is " + date);
     //   ObservableList<PersonalEvent> events = FXCollections.observableArrayList();
        events.add(personalEvent);
        // events.add(new PersonalEvent(formatter.parse("2021-10-02 11-40 am"), "Bday", "Celebration needed!"));
        //  events.add(new PersonalEvent(formatter.parse("2021-07-01 12-00 pm"), "FDGHJKKJHGFHJKJHGFGHJK", "Celebration needed fghjkgfghjkhg!"));
       // controller.addTreeTableItem(events);
    }

    public void cancelEventAdditionButtonOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) CancelEventAdditionButton.getScene().getWindow();
        stage.close();
    }
}
