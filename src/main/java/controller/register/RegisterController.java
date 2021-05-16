package controller.register;

import crypto.sha256manager;
import domain.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File; //image view
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
            userType = "Event Organizer User";
            userTypeSelected = true;
            basicUserCheckbox.setSelected(false);
        } else {
            registrationMessageLabel.setText("Please choose a user type");
            userTypeSelected = false;
        }
    }

    public void setBasicUserCheckboxOnAction(ActionEvent actionEvent) {
        if (basicUserCheckbox.isSelected()) {
            userType = "Basic User";
            userTypeSelected = true;
            eventOrganizerUserCheckbox.setSelected(false);
        } else {
            registrationMessageLabel.setText("Please choose a user type");
            userTypeSelected = false;
        }
    }


    public void closeButtonOnAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public User CreateUser() {
        switch (userType) {
            case "Basic User":
                return new BasicUser(usernameTextField.getText(), setPasswordField.getText(),
                        firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(),userType);
            case "Event Organizer User":
                return new EventOrganizerUser(usernameTextField.getText(), setPasswordField.getText(),
                        firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(),userType);
            default:
                registrationMessageLabel.setText(userType + "is not recognized!");
                return null;
        }
    }

    public void InsertNewRecord(Connection connection, User user) {

        //System.out.println("user type : " + user.getClass().getTypeName());

        try {
            //insert fields needs no special treatment
            String insertFields = "INSERT INTO \"user\" ( \"firstName\", \"lastName\", \"email\", \"username\", \"password\", \"type\") VALUES ('";

            //hash the password
            String hashedPassword = sha256manager.SHA256(user.password);
            System.out.println("Pass is : " + user.password + ",SHA256 of password is: " + hashedPassword);

            //generate queries
            String insertValues = user.firstName + "','" + user.lastName + "','" + user.email + "','" + user.username + "','" + hashedPassword + "','" + userType + "')";
            String insertToRegister = insertFields + insertValues;

            Statement statement = connection.createStatement();
            statement.executeUpdate(insertToRegister);
            registrationMessageLabel.setText("                        User registered successfully!");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            registrationMessageLabel.setText("                        Internal error! Unable to hash password!");
        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        ReturnToLoginStage();

    }

    public Connection InitializeDatabaseConnection() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        return connectDB;

    }
    public void ReturnToLoginStage() {
        closeButtonOnAction();
        try {
            Stage loginStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
            loginStage.setResizable(false);
            //loginStage.initStyle(StageStyle.DECORATED);
            loginStage.setScene(new Scene(root, 520, 400));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
