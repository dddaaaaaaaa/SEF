package controller.user;

import domain.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static javafx.scene.paint.Color.color;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;

public class AddRelativeEventController {
    @FXML
    private Button createButton;


    @FXML
    private TextField nameTextField;

    @FXML
    private TextField locationTextField;

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

    public void createButtonOnAction(ActionEvent actionEvent)
    {
        System.out.println("Add event button clicked!");

        boolean isSuccess = createNewRelativeEvent();

        if(isSuccess)
        {
            statusLabel.setTextFill(color(0, 1, 0));
            statusLabel.setText("Successfully created new event!");
        }

        else
        {
            statusLabel.setTextFill(color(1, 0, 0));
            statusLabel.setText("Failed to create event. Check input and try again.");
        }
    }

    private boolean createNewRelativeEvent()
    {
        //Debug
        System.out.println("Create relative event called!");

        long timeOffset = 0;

        //check name
        if(nameTextField.getText().isEmpty() || nameTextField.getText().length() > 100)
            return false;

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
            return false;

        long epochTime = Instant.now().getEpochSecond();

        System.out.println("Target time is " + timeOffset + " seconds from now.");
        System.out.println("That is " + (epochTime + timeOffset) + " unix time, or " + Instant.ofEpochSecond(epochTime + timeOffset));

        //database
        String insertFields = "INSERT INTO \"personalEvents\" ( \"username\", \"eventname\", \"duedate\") VALUES ('";
        String insertValues = "testname','" + nameTextField.getText() + "','" + (epochTime + timeOffset) + "')";
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
            return false;
        }

        return true;
    }
}
