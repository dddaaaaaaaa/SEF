package test;

import controller.register.RegisterController;
import domain.*;
import exceptions.InvalidCredentialsRegistration;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.junit.Before;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.AssertJUnit.assertEquals;


public class UserTest {

    public static final String TEST_USERNAME = "testUser";
    public static final String TEST_PASSWORD = "testPassword";
    public static final String TEST_EMAIL = "testEmail";
    public static final String TEST_FIRSTNAME = "testFirstName";
    public static final String TEST_LASTNAME = "testLastName";
    public static final String TEST_ADDRESS = "testAddress";
    public static final String TEST_USERTYPE ="Basic User";
    public static final String TEST_USERTYPEEVENT ="Event Organizer User";
    public static final String TEST_PHONE ="0700000000";
    public static final LocalDate TEST_DATE = LocalDate.now();

    @Test
    public void testRegisterUserAlreadyExists() throws InvalidCredentialsRegistration {
        User user = new BasicUser(TEST_USERNAME,TEST_PASSWORD,TEST_FIRSTNAME,TEST_LASTNAME,TEST_EMAIL,TEST_USERTYPE);
        Register register= new Register();
        assertEquals(true,register.registerUser(user));

    }
    @Test
    public void testRegisterNewUser() throws InvalidCredentialsRegistration {
        User user = new BasicUser(TEST_USERNAME,TEST_PASSWORD,TEST_FIRSTNAME,TEST_LASTNAME,TEST_EMAIL,TEST_USERTYPE);
        Register register= new Register();
        assertEquals(true,register.registerUser(user));
    }
    @Test
    public void testPhoneNumber() {
        User user = new BasicUser(TEST_USERNAME,TEST_PASSWORD,TEST_FIRSTNAME,TEST_LASTNAME,TEST_EMAIL,TEST_USERTYPEEVENT,TEST_ADDRESS,TEST_PHONE,TEST_DATE);
        assertEquals(true,user.isNumber(user.getPhone()));
    }

    @Test
    public void testIsString(){
        User user = new BasicUser(TEST_USERNAME,TEST_PASSWORD,TEST_FIRSTNAME,TEST_LASTNAME,TEST_EMAIL,TEST_USERTYPEEVENT,TEST_ADDRESS,TEST_PHONE,TEST_DATE);
        assertEquals(true,user.isString(user.getFirstName()));
        assertEquals(true,user.isString(user.getLastName()));

    }
    @Test
    public void testIsNotString(){
        User user = new BasicUser(TEST_USERNAME,TEST_PASSWORD,"1232345",TEST_LASTNAME,TEST_EMAIL,TEST_USERTYPEEVENT,TEST_ADDRESS,TEST_PHONE,TEST_DATE);
        assertEquals(false,user.isString(user.getFirstName()));
        assertEquals(true,user.isString(user.getLastName()));

    }


}