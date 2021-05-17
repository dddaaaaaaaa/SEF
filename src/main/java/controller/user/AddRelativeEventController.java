package controller.user;

import domain.DatabaseConnection;
import domain.PersonalEvent;
import domain.User;
import domain.UserHolder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static javafx.scene.paint.Color.color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.Date;

public class AddRelativeEventController {
    @FXML
    private Button createButton;


    @FXML
    private TextField nameTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField extraTextField;

    //date and time
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField monthTextField;
    @FXML
    private TextField dayTextField;
    @FXML
    private TextField hourTextField;
    @FXML
    private TextField minuteTextField;
    @FXML
    private TextField secondTextField;

    //status indicator
    @FXML
    private Label statusLabel;

    //reference to event list
    private ObservableList<PersonalEvent> events;

    public void setTableEvents(ObservableList<PersonalEvent> events)
    {
        this.events = events;
    }

    public void createButtonOnAction(ActionEvent actionEvent)
    {
        boolean isSuccess = createNewRelativeEvent();

        if(isSuccess)
        {
            statusLabel.setTextFill(color(0, 1, 0));
            statusLabel.setText("Successfully created new event!");
        }

        else
        {
            statusLabel.setTextFill(color(1, 0, 0));
        }
    }

    private boolean createNewRelativeEvent()
    {
        //Debug
        System.out.println("Create relative event called!");

        long timeOffset = 0;

        //check name
        if(nameTextField.getText().isEmpty() || nameTextField.getText().length() > 100)
        {
            statusLabel.setText("Failed to create event. Check name input and try again.");
            return false;
        }

        //process year
        try {
            timeOffset += (long)Integer.parseInt(yearTextField.getText()) * 31557600;
        } catch (NumberFormatException nfe) {
            //empty or not a number
        }

        //process month
        try {
            timeOffset += (long)Integer.parseInt(monthTextField.getText()) * 2592000;
        } catch (NumberFormatException nfe) {
            //empty or not a number
        }

        //process day
        try {
            timeOffset += (long)Integer.parseInt(dayTextField.getText()) * 86400;
        } catch (NumberFormatException nfe) {
            //empty or not a number
        }

        //process hour
        try {
            timeOffset += (long)Integer.parseInt(hourTextField.getText()) * 3600;
        } catch (NumberFormatException nfe) {
            //empty or not a number
        }

        //process minute
        try {
            timeOffset += (long)Integer.parseInt(minuteTextField.getText()) * 60;
        } catch (NumberFormatException nfe) {
            //empty or not a number
        }

        //process second
        try {
            timeOffset += Integer.parseInt(secondTextField.getText());
        } catch (NumberFormatException nfe) {
            //empty or not a number
        }

        System.out.println("Time offset: " + timeOffset);
        if(timeOffset < 5) //too short failsafe
        {
            statusLabel.setText("Failed to create event. Time difference too low.");
            return false;
        }

        long epochTime = Instant.now().getEpochSecond();

        //System.out.println("Target time is " + timeOffset + " seconds from now.");
        //System.out.println("That is " + (epochTime + timeOffset) + " unix time, or " + Instant.ofEpochSecond(epochTime + timeOffset));

        //database
        User currentUser = UserHolder.getInstance().getUser();
        if(currentUser == null)
        {
            statusLabel.setText("Fatal error! Undefined reference to logged in user!.");
            return false;
        }

        try
        {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("INSERT INTO \"personalEvents\" ( \"username\", \"eventname\", \"duedate\", \"extra\", \"location\") VALUES (?,?,?,?,?);");
            ps.setString(1, currentUser.getUsername());
            ps.setString(2, nameTextField.getText());
            ps.setLong(3, epochTime + timeOffset);
            ps.setString(4, extraTextField.getText());
            ps.setString(5, locationTextField.getText());

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
            statusLabel.setText("SQL Error! Unable to create event!");
            return false;
        }

        //creation successful, add to list
        Date eventDate = new Date();
        eventDate.setTime((epochTime + timeOffset) * 1000);
        PersonalEvent pe = new PersonalEvent(eventDate, nameTextField.getText(), extraTextField.getText(), "myself", locationTextField.getText());
        events.add(pe);

        return true;
    }
}
