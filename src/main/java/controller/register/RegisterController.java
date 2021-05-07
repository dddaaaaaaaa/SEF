package controller.register;

import domain.*;
import javafx.application.Platform;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.File; //image view
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.lang.reflect.Field;

public class RegisterController implements Initializable {

    @FXML
    private ImageView registerImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private CheckBox basicUserCheckbox;
    @FXML
    private CheckBox eventOrganizerUserCheckbox;
    @FXML
    private ComboBox<String> comboBoxButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File registerFile = new File("src\\main\\resources\\images\\register.png");
        Image registerImage = new Image(registerFile.toURI().toString());
        registerImageView.setImage(registerImage);
        comboBoxButton.getItems().add("Basic User");
        comboBoxButton.getItems().add("Event Organizer User");


    }

    String userType = "";
    boolean userTypeSelected;

    public void registerButtonOnAction(ActionEvent actionEvent) {
        if (setPasswordField.getText().isBlank()) {
            registrationMessageLabel.setText("");
            confirmPasswordLabel.setText("Password required!");
        }
        if (userTypeSelected == false) {
            registrationMessageLabel.setText("Please chose a user type!");
            confirmPasswordLabel.setText("");
        }
        if (setPasswordField.getText().equals(confirmPasswordField.getText()) && setPasswordField.getText().isBlank() == false) {
            if (userTypeSelected) {
               // Register registration = new Register();
              //  registration.registerUser();
                User user = CreateUser();
                if (user != null) {
                    Connection connectDB = InitializeDatabaseConnection();
                    InsertNewRecord(connectDB, user);
                    confirmPasswordLabel.setText("Passwords match!");
                }
            }
        } else {
            confirmPasswordLabel.setText("Passwords do not match!");
            registrationMessageLabel.setText("");
        }
    }

    public void setEventOrganizerUserCheckboxOnAction(ActionEvent actionEvent) {
        if (eventOrganizerUserCheckbox.isSelected()) {
            userType = userType + "Event Organizer User";
            userTypeSelected = true;
        } else {
            registrationMessageLabel.setText("Please choose a user type");
            userTypeSelected = false;
        }
    }

    public void setBasicUserCheckboxOnAction(ActionEvent actionEvent) {
        if (basicUserCheckbox.isSelected()) {
            userType = userType + "Basic User";
            userTypeSelected = true;
        } else {
            registrationMessageLabel.setText("Please choose a user type");
            userTypeSelected = false;
        }
    }


    public void closeButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public User CreateUser() {

        switch (userType) {
            case "Basic User":
                return new BasicUser(firstNameTextField.getText(), lastNameTextField.getText(),
                        emailTextField.getText(), usernameTextField.getText(),
                        setPasswordField.getText());
            case "Event Organizer User":
                return new EventOrganizerUser(firstNameTextField.getText(), lastNameTextField.getText(),
                        emailTextField.getText(), usernameTextField.getText(),
                        setPasswordField.getText());
            default:
                registrationMessageLabel.setText(userType + "is not recognized!");
                return null;
        }
    }

    public void InsertNewRecord(Connection connection, User user) {

        //System.out.println("user type : " + user.getClass().getTypeName());
        String insertFields = "INSERT INTO \"user\" ( \"firstName\", \"lastName\", \"email\", \"username\", \"password\", \"type\") VALUES ('";
        String insertValues = user.firstName + "','" + user.lastName + "','" + user.email + "','" + user.username + "','" + user.password + "','" + userType + "')";
        String insertToRegister = insertFields + insertValues;


        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertToRegister);
            registrationMessageLabel.setText("                        User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public Connection InitializeDatabaseConnection() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        return connectDB;

    }
}
