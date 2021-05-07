package controller.register;

import domain.DatabaseConnection;
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

public class RegisterController implements Initializable {
    String userType;
    boolean userTypeSelected;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File registerFile = new File("C:\\Users\\Admin\\Desktop\\SEF\\remind-me-app\\src\\main\\resources\\images\\register.png");
        Image registerImage = new Image(registerFile.toURI().toString());
        registerImageView.setImage(registerImage);

    }

    public void registerButtonOnAction(ActionEvent actionEvent) {
        if (setPasswordField.getText().equals(confirmPasswordField.getText())) {
            if (userTypeSelected) {
                registerUser();
                confirmPasswordLabel.setText("Passwords match!");
                registrationMessageLabel.setText("                        User registered successfully!");
            } else {
                registrationMessageLabel.setText("Please chose a user type!");
            }
        } else {
            confirmPasswordLabel.setText("Passwords do not match!");
            registrationMessageLabel.setText("");
        }
        registerUser();
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

    public void registerUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = setPasswordField.getText();

        String insertFields = "";
        String insertValues = "";
        String insertToRegister = insertFields + insertValues;

    }
}
