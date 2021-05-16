package controller.event;

import domain.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static javafx.scene.paint.Color.color;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;

public class AddEventController {
    @FXML
    private Button createButton;

    @FXML
    private CheckBox relativeTimeCheckbox;

    @FXML
    private TextField nameTextField;

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
        //Debug
        boolean isRelativeTime = relativeTimeCheckbox.isSelected();
        System.out.println("Add event button clicked! Is selected: " + isRelativeTime);

        boolean isSuccess;
        if(isRelativeTime)
            isSuccess = createNewRelativeEvent();

        else
            isSuccess = createNewAbsoluteEvent();

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

    private boolean createNewAbsoluteEvent()
    {
        //Debug
        System.out.println("Create absolute event called!");

        int year = 1970;
        int month = 1;
        int day = 1;

        int h = 0;
        int m = 0;
        int s = 0;

        //check name
        if(nameTextField.getText().isEmpty() || nameTextField.getText().length() > 100)
            return false;

        //process year
        try {
            year = Integer.parseInt(yearTextField.getText());
            if(year < 1970)
                return false;
        } catch (NumberFormatException nfe) {
            //empty or not a number
            return false;
        }

        //process month
        try {
            month = Integer.parseInt(monthTextField.getText());
            if(month < 0 || month > 12)
                return false;
        } catch (NumberFormatException nfe) {
            //empty or not a number
            return false;
        }

        //process day
        try {
            day = Integer.parseInt(dayTextField.getText());
            if (day < 1)
                return false;

            //determine max day in month
            int maxDay = 28;
            switch (month)
            {
                case 1:
                    maxDay = 31;
                    break;

                case 2:
                    if(year % 4 == 0)
                        maxDay = 29;
                    else
                        maxDay = 28;
                    break;

                case 3:
                    maxDay = 31;
                    break;

                case 4:
                    maxDay = 30;
                    break;

                case 5:
                    maxDay = 31;
                    break;

                case 6:
                    maxDay = 30;
                    break;

                case 7:
                    maxDay = 31;
                    break;

                case 8:
                    maxDay = 31;
                    break;

                case 9:
                    maxDay = 30;
                    break;

                case 10:
                    maxDay = 31;
                    break;

                case 11:
                    maxDay = 30;
                    break;

                case 12:
                    maxDay = 31;
                    break;

                default:    //should not reach here
                    return false;
            }

            if(day > maxDay)
                return false;

        } catch (NumberFormatException nfe) {
            //empty or not a number
            return false;
        }

        //process hour
        try {
            h = Integer.parseInt(hourTextField.getText());

            if(h < 0 || h > 24)
                return false;
        } catch (NumberFormatException nfe) {
            //empty or not a number
            return false;
        }

        //process minute
        try {
            m = Integer.parseInt(minuteTextField.getText());
            if(m < 0 || m > 60)
                return false;
        } catch (NumberFormatException nfe) {
            //empty or not a number
            return false;
        }

        //process second
        try {
            s = Integer.parseInt(secondTextField.getText());
            if(s < 0 || s > 60)
                return false;
        } catch (NumberFormatException nfe) {
            //empty or not a number
            return false;
        }

        //if we reach here, we have a valid input
        LocalDateTime eventDateTime = LocalDateTime.of(year, month, day, h, m, s);
        System.out.println("Date and time of event: " + eventDateTime);

        //get system time zone
        Instant tempInstant = Instant.now();
        ZoneId systemZone = ZoneId.systemDefault();
        ZoneOffset myTimeZoneOffset = systemZone.getRules().getOffset(tempInstant);

        long epochTime = eventDateTime.toEpochSecond(myTimeZoneOffset);
        System.out.println("Converted to UNIX time: " + epochTime);

        //database
        String insertFields = "INSERT INTO \"personalEvents\" ( \"username\", \"eventname\", \"duedate\") VALUES ('";
        String insertValues = "testname','" + nameTextField.getText() + "','" + epochTime + "')";
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
