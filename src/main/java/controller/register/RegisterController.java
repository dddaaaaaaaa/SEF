package controller.register;

import crypto.sha256manager;
import domain.*;
import exceptions.InvalidCredentialsRegistration;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.File; //image view
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView registerImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Button registerButton;
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
    private TextField addressTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private DatePicker dateField;
    @FXML
    private CheckBox basicUserCheckbox;
    @FXML
    private CheckBox eventOrganizerUserCheckbox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File registerFile = new File("src\\main\\resources\\images\\register.png");
        Image registerImage = new Image(registerFile.toURI().toString());
        registerImageView.setImage(registerImage);
    }

    String userType = "";
    boolean userTypeSelected;

    public void registerButtonOnAction(ActionEvent actionEvent) throws InvalidCredentialsRegistration {
        if (usernameTextField.getText().isBlank() || setPasswordField.getText().isBlank() || emailTextField.getText().isBlank())
        {
            confirmPasswordLabel.setText("Username, email and password are required!");
            return;
        }

        if (userTypeSelected == false)
        {
            confirmPasswordLabel.setText("Please chose a user type!");
            return;
        }

        if (setPasswordField.getText().equals(confirmPasswordField.getText()))
        {
            User user = CreateUser();
            if (user != null)
            {
                //checks for event organizer
                if(user instanceof EventOrganizerUser)
                {
                    if(firstNameTextField.getText().isBlank() || lastNameTextField.getText().isBlank() ||
                    addressTextField.getText().isBlank() || phoneTextField.getText().isBlank() ||
                    dateField.getValue() == null)
                    {
                        confirmPasswordLabel.setText("All fields required for event organizer!");
                        return;
                    }

                    if(phoneTextField.getText().length() > 12 || !phoneTextField.getText().matches("[0-9]*"))
                    {
                        confirmPasswordLabel.setText("Invalid phone input!");
                        return;
                    }
                }
                Connection connectDB = InitializeDatabaseConnection();
                InsertNewRecord(connectDB, user);
                //confirmPasswordLabel.setText("Passwords match!");
            }
            else
                confirmPasswordLabel.setText("Internal error - cannot create user object!");
        }
        else
        {
            confirmPasswordLabel.setText("Passwords do not match!");
        }


    }

    public void setEventOrganizerUserCheckboxOnAction(ActionEvent actionEvent) {
        if (eventOrganizerUserCheckbox.isSelected()) {
            userType = "Event Organizer User";
            userTypeSelected = true;
            basicUserCheckbox.setSelected(false);
        } else {
            confirmPasswordLabel.setText("Please choose a user type");
            userTypeSelected = false;
        }
    }

    public void setBasicUserCheckboxOnAction(ActionEvent actionEvent) {
        if (basicUserCheckbox.isSelected()) {
            userType = "Basic User";
            userTypeSelected = true;
            eventOrganizerUserCheckbox.setSelected(false);
        } else {
            confirmPasswordLabel.setText("Please choose a user type");
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
                        firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), userType);
            case "Event Organizer User":
                return new EventOrganizerUser(usernameTextField.getText(), setPasswordField.getText(),
                        firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), userType,
                        addressTextField.getText(), phoneTextField.getText(), dateField.getValue());
            default:
                confirmPasswordLabel.setText(userType + "is not recognized!");
                return null;
        }
    }

    public void InsertNewRecord(Connection connection, User user) throws InvalidCredentialsRegistration {

        Register register = new Register();
        if (register.registerUser(user))
        {
            //System.out.println("Starting registration");
            try {
                //insert fields needs no special treatment
                PreparedStatement ps = connection.prepareStatement("INSERT INTO \"user\" ( \"firstName\", \"lastName\"," +
                        " \"email\", \"username\", \"password\", \"type\", \"address\", \"phone\", \"birth\") VALUES (?,?,?,?,?,?,?,?,?);");
                //hash the password
                String hashedPassword = sha256manager.SHA256(user.getPassword());

                //generate queries
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getUsername());
                ps.setString(5, hashedPassword);
                ps.setString(6, userType);
                ps.setString(7, user.getAddress());
                ps.setString(8, user.getPhone());
                ps.setString(9, user.getBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

                ps.executeUpdate();
                confirmPasswordLabel.setText("User registered successfully!");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                e.getCause();
                confirmPasswordLabel.setText("Internal error! Unable to hash password!");
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
                confirmPasswordLabel.setText("Database error!");
            }
            ReturnToLoginStage();
        } else
            confirmPasswordLabel.setText("Username already exists!");
    }

    public Connection InitializeDatabaseConnection()
    {
        return new DatabaseConnection().getConnection();
    }

    public void ReturnToLoginStage()
    {
        closeButtonOnAction();
    }
}
