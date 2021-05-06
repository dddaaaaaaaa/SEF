package controller.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

//import java.sql.SQLOutput;

public class LoginController  {
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
    private TextField enterPasswordField;

    // @Override
    /*public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("resources/images/sigla.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);


        File lockFile = new File("resources/images/lacat.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }*/
    public void loginButtonOnAction(ActionEvent actionEvent) {
       // System.out.println("Login Button On Action!");
        loginMessageLabel.setText("You tried to login!");
        if(usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false){
           // loginMessageLabel.setText("Username or password does not exist!");
            validateLogin();
        }
        else
        {
            loginMessageLabel.setText("Please enter usename and password.");
        }

    }

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        System.out.println("Cancel Button On Action!");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin(){

    }
}
