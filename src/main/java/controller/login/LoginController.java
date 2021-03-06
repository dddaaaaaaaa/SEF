package controller.login;

import controller.user.UserViewController;
import crypto.sha256manager;
import domain.*;
import exceptions.InvalidCredentialsRegistration;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

//import java.sql.SQLOutput;

public class LoginController implements Initializable {

    boolean registrationRequired = false;

    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Hyperlink registerHyperlink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginButton.setDefaultButton(true);
    }

    public void loginButtonOnAction(ActionEvent actionEvent) {
        if (usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false) {
            if (validateLogin())
                createUserViewStage();
        } else {
            loginMessageLabel.setText("Please enter username and password.");
        }


    }

    public User retrieveUserFromDatabase(String username) throws SQLException {
        User currentUser = null;
        try {

            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("SELECT * FROM \"user\" WHERE username = ?;");
            ps.setString(1, username);
            ResultSet queryResult = ps.executeQuery();

            //data available here
            while (queryResult.next()) {
                String FirstName = queryResult.getString(2);
                String LastName = queryResult.getString(3);
                String Email = queryResult.getString(4);
                String Username = queryResult.getString(5);
                String Type = queryResult.getString(7);
                String Password = queryResult.getString(6);
                String address = queryResult.getString(8);
                String phone = queryResult.getString(9);
                String dateStr = queryResult.getString(10);
                LocalDate birthdate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));


                switch (queryResult.getString(7)) {
                    case "Event Organizer User":
                        // public BasicUser(String username, String password, String firstName, String lastName, String email, String user)
                        currentUser = new EventOrganizerUser(Username, Password, FirstName, LastName, Email,
                                Type, address, phone, birthdate);
                        break;
                    case "Basic User":
                        currentUser = new BasicUser(Username, Password, FirstName, LastName, Email,
                                Type, address, phone, birthdate);
                        break;

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
            System.out.println("Querying user events failed!");
        }
         UserHolder.getInstance().setUser(currentUser);

        return currentUser;
    }


    public void cancelButtonOnAction(ActionEvent actionEvent) {
        System.out.println("Cancel Button On Action!");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public void registerHyperlinkOnAction(ActionEvent actionEvent) {
        if (registerHyperlink.isVisited())
            registrationRequired = true;
        if (registrationRequired)
            createRegistrationStage();

    }

    public boolean validateLogin() {
        boolean isSuccess = false;
        DatabaseConnection connectNow = new DatabaseConnection();
        // Connection connectDB = connectNow.getConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            //hash password
            String hashedPassword = sha256manager.SHA256(enterPasswordField.getText());
            //System.out.println("Pass is : " + enterPasswordField.getText() + ", SHA256 of password is: " + hashedPassword);

            //do query
            PreparedStatement ps = connectDB.prepareStatement("SELECT count (1) FROM \"user\" WHERE username = ? AND password = ?;");
            ps.setString(1, usernameTextField.getText());
            ps.setString(2, hashedPassword);
            ResultSet queryResult = ps.executeQuery();

            while (queryResult.next()) {
                // System.out.println(queryResult.getInt(1));
                if (queryResult.getInt(1) == 1) {
                    //loginMessageLabel.setText("You logged in successfully!");
                    isSuccess = true;
                } else {
                    loginMessageLabel.setText("Invalid login. Try again!");
                }
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Internal error! Unable to hash password!");
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
            loginMessageLabel.setText("Database error!");
        }

        return isSuccess;
    }

    public void createRegistrationStage() {
        try {
            Stage registerStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/registration.fxml"));
            registerStage.setResizable(false);
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 553, 591));
            registerStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void createUserViewStage() {
        try {
            User user = retrieveUserFromDatabase(usernameTextField.getText());
            Stage UserViewStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/UserView.fxml"));
            //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/UserView.fxml"));
            //UserViewStage.setResizable(false);
            Parent root = loader.load();
            UserViewStage.initStyle(StageStyle.DECORATED);
            Scene scene = new Scene(root, 800, 650);

            // UserViewController userViewController = loader.getController();
            // User user = retrieveUserFromDatabase(usernameTextField.getText());

            UserViewStage.setScene(scene);
            //UserViewStage.showAndWait();
            UserViewStage.show();

            //now logged in, close login window
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

}
