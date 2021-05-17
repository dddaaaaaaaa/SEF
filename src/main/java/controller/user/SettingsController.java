package controller.user;

import domain.UserViewInterface;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class SettingsController extends UserViewInterface {

    @FXML
    private TextField FirstNameTextField, LastNameTextField, EmailTextField, AddressTextField, PhoneTextField, OcupationTextField;
    @FXML
    private DatePicker BirthDate;

   public void Test()
   {
   }
}
