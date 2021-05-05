package controller.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.SQLOutput;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;

    public void loginButtonOnAction(ActionEvent actionEvent) {
        System.out.println("Login Button On Action!");
    }

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        System.out.println("Cancel Button On Action!");
    }
}
