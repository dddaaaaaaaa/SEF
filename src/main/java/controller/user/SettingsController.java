package controller.user;

import crypto.sha256manager;
import domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.color;


public class SettingsController extends UserViewInterface implements Initializable {

    @FXML
    private TextField FirstNameTextField, LastNameTextField, EmailTextField, AddressTextField, PhoneTextField;
    @FXML
    private PasswordField NewPassTextField, ConfirmNewPassTextField;
    @FXML
    private CheckBox NewPasswordCheckbox;
    @FXML
    private Label NewPassLabel;
    @FXML
    private DatePicker BirthDate;
    private User currentUser;
    @FXML
    private Label MandatoryLabel, UserTypeLabel;
    //private EventOrganizerUser eventUser;
    //private BasicUser basicUser;
    @FXML
    private Button UpgradeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = UserHolder.getInstance().getUser();
        MandatoryLabel.setTextFill(color(1, 0, 0));
        InitializeSettingsFields();
    }

    public void InitializeSettingsFields() {
        FirstNameTextField.setText(currentUser.getFirstName());
        LastNameTextField.setText(currentUser.getLastName());
        EmailTextField.setText(currentUser.getEmail());
        UserTypeLabel.setText(currentUser.getUser());
        AddressTextField.setText(currentUser.getAddress());
        PhoneTextField.setText(currentUser.getPhone());
        BirthDate.setValue(currentUser.getBirth());
        if (currentUser instanceof BasicUser)
            UpgradeButton.setDisable(false);
    }

    public void updateCredentialsButtonOnAction(ActionEvent actionEvent) {
        String phone = PhoneTextField.getText();

        if(phone != null && (phone.length() > 12 || !phone.matches("[0-9]*")))
        {
            MandatoryLabel.setText("Invalid phone input!");
            return;
        }

        if(EmailTextField.getText().isBlank())
        {
            MandatoryLabel.setText("Email can't be blank!");
            return;
        }

        currentUser.setFirstName(FirstNameTextField.getText());
        currentUser.setLastName(LastNameTextField.getText());
        currentUser.setAddress(AddressTextField.getText());
        currentUser.setEmail(EmailTextField.getText());
        currentUser.setPhone(phone);
        if(BirthDate.getValue() != null)
            currentUser.setBirth(BirthDate.getValue());

        try
        {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("UPDATE \"user\" SET \"firstName\" = ?, " +
                            "\"lastName\" = ?, \"email\" = ?, \"address\" = ?, \"phone\" = ?, \"birth\" = ? " +
                            "WHERE username = ?;");
            ps.setString(1, currentUser.getFirstName());
            ps.setString(2, currentUser.getLastName());
            ps.setString(3, currentUser.getEmail());
            ps.setString(4, currentUser.getAddress());
            ps.setString(5, currentUser.getPhone());
            ps.setString(6, currentUser.getBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            ps.setString(7, currentUser.getUsername());

            ps.executeUpdate();

            if(NewPasswordCheckbox.isSelected())
                if(!updatePassword(connectDB))
                    return;


            MandatoryLabel.setTextFill(color(0, 1, 0));
            MandatoryLabel.setText("Credential update successful!");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
            MandatoryLabel.setText("SQL Error! Unable to update credentials!");
        }
    }

    public void UpgradeButtonOnAction(ActionEvent actionEvent) {
        if (FirstNameTextField.getText().isBlank() || LastNameTextField.getText().isBlank() ||
                AddressTextField.getText().isBlank() || PhoneTextField.getText().isBlank() ||
                BirthDate.getValue() == null) {
            MandatoryLabel.setText("Full name, address, phone number and birthdate are required for upgrade!");
        }
        else {
            String phone = PhoneTextField.getText();

            if(phone.length() > 12 || !phone.matches("[0-9]*"))
            {
                MandatoryLabel.setText("Invalid phone input!");
                return;
            }

            currentUser.setFirstName(FirstNameTextField.getText());
            currentUser.setLastName(LastNameTextField.getText());
            currentUser.setAddress(AddressTextField.getText());
            currentUser.setEmail(EmailTextField.getText());
            currentUser.setPhone(phone);
            currentUser.setBirth(BirthDate.getValue());
            //String occupation = OccupationTextField.getText();

            try
            {
                Connection connectDB = new DatabaseConnection().getConnection();
                PreparedStatement ps = connectDB.prepareStatement("UPDATE \"user\" SET \"firstName\" = ?, " +
                        "\"lastName\" = ?, \"email\" = ?, \"type\" = ?, \"address\" = ?, \"phone\" = ?, \"birth\" = ? " +
                        "WHERE username = ?;");
                ps.setString(1, currentUser.getFirstName());
                ps.setString(2, currentUser.getLastName());
                ps.setString(3, currentUser.getEmail());
                ps.setString(4, "Event Organizer User");
                ps.setString(5, currentUser.getAddress());
                ps.setString(6, currentUser.getPhone());
                ps.setString(7, currentUser.getBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                ps.setString(8, currentUser.getUsername());

                ps.executeUpdate();

                UpgradeButton.setDisable(true);

                if(NewPasswordCheckbox.isSelected())
                    if(!updatePassword(connectDB))
                        return;

                MandatoryLabel.setTextFill(color(0, 1, 0));
                MandatoryLabel.setText("Upgrade successful! Relog to see changes!");
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                e.getCause();
                MandatoryLabel.setText("SQL Error! Unable to update credentials!");
            }
        }
    }

    public void setPasswordCheckboxOnAction(ActionEvent actionEvent)
    {
        if(NewPasswordCheckbox.isSelected())
        {
            NewPassTextField.setDisable(false);
            ConfirmNewPassTextField.setDisable(false);
            NewPassLabel.setDisable(false);
        }

        else
        {
            NewPassTextField.setDisable(true);
            ConfirmNewPassTextField.setDisable(true);
            NewPassLabel.setDisable(true);
        }
    }

    public boolean updatePassword(Connection connectDB) throws SQLException
    {
        if(NewPassTextField.getText().isBlank())
        {
            MandatoryLabel.setText("Password field is blank!");
            return false;
        }

        if(!NewPassTextField.getText().equals(ConfirmNewPassTextField.getText()))
        {
            MandatoryLabel.setText("Passwords do not match!");
            return false;
        }

        try
        {
            String hashedPassword = sha256manager.SHA256(NewPassTextField.getText());

            PreparedStatement ps = connectDB.prepareStatement("UPDATE \"user\" SET \"password\" = ? " +
                    "WHERE username = ?;");
            ps.setString(1, hashedPassword);
            ps.setString(2, currentUser.getUsername());

            ps.executeUpdate();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            e.getCause();
            MandatoryLabel.setText("Internal error! Unable to hash new password!");
            return false;
        }

        return true;
    }
}
