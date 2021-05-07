package controller.login;

import domain.DatabaseConnection;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
        File brandingFile = new File("src\\main\\resources\\images\\sigla.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);


        File lockFile = new File("src\\main\\resources\\images\\lacat.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void loginButtonOnAction(ActionEvent actionEvent) {
        //System.out.println("Login Button On Action!");
        loginMessageLabel.setText("You tried to login!");
        if (usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false) {
            // loginMessageLabel.setText("Username or password does not exist!");
            /*if (registrationRequired == true)
                createRegistrationStage();*/
            //else
                validateLogin();
        } else {
            loginMessageLabel.setText("Please enter username and password.");
        }

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
        if (registrationRequired == true)
            createRegistrationStage();

    }

    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        // Connection connectDB = connectNow.getConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM \"user\" WHERE username = '" + usernameTextField.getText() + "' AND password = '" + enterPasswordField.getText() + "';";

        try {

            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    loginMessageLabel.setText("You logged in successfully!");
                } else {
                    loginMessageLabel.setText("Invalid login. Try again!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void createRegistrationStage() {
        try {
            Stage registerStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/registration.fxml"));
            registerStage.setResizable(false);
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 553, 591));
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

}
