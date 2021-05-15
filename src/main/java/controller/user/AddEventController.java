package controller.user;

import domain.DatabaseConnection;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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

    public void addEventButtonOnAction(javafx.event.ActionEvent actionEvent)
    {
        //get date
        LocalDate localDate = DateField.getValue();

        //get time
        int hour = 0;
        int minute = 0;
        //get hour
        try {
            hour= Integer.parseInt(HourTextField.getText());
        } catch (NumberFormatException nfe) {
            //empty or not a number
            hour = 0;
        }

        //process minute
        try {
            minute = Integer.parseInt(MinuteTextField.getText());
        } catch (NumberFormatException nfe) {
            //empty or not a number
            hour = 0;
            //TODO add warnings!
        }

        Calendar cal = Calendar.getInstance();
        //cal.setTime(date);
        cal.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.MILLISECOND, 0);

        //calendar complete, create event
        Date date = cal.getTime();
        PersonalEvent personalEvent = new PersonalEvent(date, EventNameTextField.getText(), ObservationsTextField.getText(), HostTextField.getText(), LocationTextField.getText());

        if (!(EventNameTextField.getText().isBlank()) && !(DateField.getValue() == null) && !(ObservationsTextField).getText().isBlank()) {
            try {
                getPersonalEvent(personalEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            MandatoryLabelField.setText("All fields are mandatory!");
        }
    }

    public void getPersonalEvent(PersonalEvent personalEvent) throws ParseException {
        // PersonalEventsListController controller = new PersonalEventsListController();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = DateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //this line adds the event to the GUI list
        events.add(personalEvent);

        //add event to the database
        dbinsert(personalEvent);

        //event added - close stage
        Stage stage = (Stage) AddEventButton.getScene().getWindow();
        stage.close();

    }

    private void dbinsert(PersonalEvent p)
    {
        if(p == null)
        {
            System.out.println("Fatal error! Undefined reference to personal event!");
            MandatoryLabelField.setText("Fatal error! Undefined reference to personal event!");
            return;
        }

        String username = p.getHost();  //TODO get actual username
        String eventname = p.getEventName();
        long duedate = (p.getDate().getTime() / 1000);
        String extra = p.getObservations();
        String location = p.getLocation();

        //database
        String insertFields = "INSERT INTO \"personalEvents\" ( \"username\", \"eventname\", \"duedate\", \"extra\", \"location\") VALUES ('";
        String insertValues = username + "','" + eventname + "','" + duedate + "','" + extra +"','" + location + "')";
        String insertToEvents = insertFields + insertValues;

        try {
            Connection connectDB = new DatabaseConnection().getConnection();
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToEvents);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
            MandatoryLabelField.setText("SQL Error! Unable to create event!");
        }
    }
    public void cancelEventAdditionButtonOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) CancelEventAdditionButton.getScene().getWindow();
        stage.close();
    }
}
