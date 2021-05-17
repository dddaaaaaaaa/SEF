package controller.user;

import domain.DatabaseConnection;
import domain.PersonalEvent;
import domain.UserHolder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static javafx.scene.paint.Color.color;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src\\main\\resources\\images\\sigla\\event.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    public void addEventButtonOnAction(javafx.event.ActionEvent actionEvent) {
        if (DateField.getValue() == null) {
            //date field is empty
            MandatoryLabelField.setTextFill(color(1, 0, 0));
            MandatoryLabelField.setText("Date is mandatory!");
            return;
        }

        //get date
        LocalDate localDate = DateField.getValue();

        //get time
        int hour;
        int minute;
        //get hour
        try {
            hour = Integer.parseInt(HourTextField.getText());
        } catch (NumberFormatException nfe) {
            //empty or not a number
            hour = 0;

            Optional<ButtonType> result = new Alert(Alert.AlertType.WARNING, "Hour field is blank/invalid! Continue?", ButtonType.NO, ButtonType.YES).showAndWait();
            if (result.isEmpty())
                return;
            else if (result.get() == ButtonType.YES) {
            } else if (result.get() == ButtonType.NO)
                return;
        }

        //process minute
        try {
            minute = Integer.parseInt(MinuteTextField.getText());
        } catch (NumberFormatException nfe) {
            //empty or not a number
            minute = 0;

            Optional<ButtonType> result = new Alert(Alert.AlertType.WARNING, "Minute field is blank/invalid! Continue?", ButtonType.NO, ButtonType.YES).showAndWait();
            if (result.isEmpty())
                return;
            else if (result.get() == ButtonType.YES) {
            } else if (result.get() == ButtonType.NO)
                return;
        }

        Calendar cal = Calendar.getInstance();
        //cal.setTime(date);
        cal.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        //calendar complete, create event
        Date date = cal.getTime();

        //do not create events in the past
        long epochTime = Instant.now().getEpochSecond();
        if(date.getTime() / 1000 < epochTime)
        {
            MandatoryLabelField.setText("Can't create events in the past!");
            return;
        }

        PersonalEvent personalEvent = new PersonalEvent(date, EventNameTextField.getText(), ObservationsTextField.getText(), UserHolder.getInstance().getUser().getUsername(), LocationTextField.getText());

        if (!(EventNameTextField.getText().isBlank()) && !(DateField.getValue() == null)) {
            try {
                getPersonalEvent(personalEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            MandatoryLabelField.setText("Name and date are mandatory!");
        }
    }

    public void getPersonalEvent(PersonalEvent personalEvent) throws ParseException {

        //this line adds the event to the GUI list
        events.add(personalEvent);

        //add event to the database
        dbinsert(personalEvent);

        //event added - close stage
        Stage stage = (Stage) AddEventButton.getScene().getWindow();
        stage.close();

    }

    private void dbinsert(PersonalEvent p) {
        if (p == null) {
            //System.out.println("Fatal error! Undefined reference to personal event!");
            MandatoryLabelField.setText("Fatal error! Undefined reference to personal event!");
            return;
        }

        if(UserHolder.getInstance().getUser().getUsername() == null)
        {
            MandatoryLabelField.setText("Fatal error! Undefined reference to logged in user!");
            return;
        }

        try
        {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("INSERT INTO \"personalEvents\" ( \"username\", \"eventname\", \"duedate\", \"extra\", \"location\", \"host\") VALUES (?,?,?,?,?,?);");
            ps.setString(1, UserHolder.getInstance().getUser().getUsername());
            ps.setString(2, p.getEventName());
            ps.setLong(3, p.getDate().getTime() / 1000);
            ps.setString(4, p.getObservations());
            ps.setString(5, p.getLocation());
            ps.setString(6, UserHolder.getInstance().getUser().getUsername());

            ps.executeUpdate();

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
