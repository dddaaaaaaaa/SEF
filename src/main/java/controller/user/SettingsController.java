package controller.user;

import domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class SettingsController extends UserViewInterface implements Initializable {

    @FXML
    private TextField FirstNameTextField, LastNameTextField, EmailTextField, AddressTextField, PhoneTextField, OccupationTextField;
    @FXML
    private DatePicker BirthDate;
    private User currentUser;
    @FXML
    private Label MandatoryLabel;
    private EventOrganizerUser eventUser;
    private BasicUser basicUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = UserHolder.getInstance().getUser();
        System.out.println("This is settings user " + currentUser.getUsername());
        InitializeSettingsFields();


    }

    public void InitializeSettingsFields() {
        FirstNameTextField.setText(currentUser.getFirstName());
        LastNameTextField.setText(currentUser.getLastName());
        EmailTextField.setText(currentUser.getEmail());
        if (currentUser.getUser().equals("Event Organizer User")) {
            eventUser = (EventOrganizerUser) currentUser;
            if (AddressTextField.getText().isBlank() || PhoneTextField.getText().isBlank() || BirthDate.getValue() == null || OccupationTextField.getText().isBlank())
                MandatoryLabel.setText("Mandatory fields!");
            else {
                //set fields from database
                /*AddressTextField.setText(eventUser.getAddress());
                //System.out.println("\n"+ eventUser.getBirth());
                //BirthDate.setValue(eventUser.getBirth());
                OccupationTextField.setText(eventUser.getOccupation());
                PhoneTextField.setText(eventUser.getPhone());
                
                 */
            }
        }

    }

    public void updateCredentialsButtonOnAction(ActionEvent actionEvent) {
        if (AddressTextField.getText().isBlank() || PhoneTextField.getText().isBlank() || OccupationTextField.getText().isBlank())
            MandatoryLabel.setText("Mandatory fields!");
        else {
            String address = AddressTextField.getText();
            String phone = PhoneTextField.getText();
            String occupation = OccupationTextField.getText();
            LocalDate date = BirthDate.getValue();
            eventUser.setAddress(address);
            //eventUser.setBirth(BirthDate.getValue());
            //System.out.println(BirthDate.getValue());
            eventUser.setOccupation(occupation);
            eventUser.setPhone(phone);

        }
    }
}
