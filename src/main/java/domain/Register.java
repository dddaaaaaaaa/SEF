package domain;

import exceptions.InvalidCredentialsRegistration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Register {
    public String firstName, lastName, email, username;

    public boolean registerUser(User user) throws InvalidCredentialsRegistration {
    String queryString = "SELECT * FROM \"user\"";    //TODO query only my events
        try {
            Connection connectDB = new DatabaseConnection().getConnection();
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(queryString);

            //data available here
            while (queryResult.next()) {
                //String FirstName = queryResult.getString(2);
               // String LastName = queryResult.getString(3);
                String Email = queryResult.getString(4);
                String Username = queryResult.getString(5);
              //  String Type = queryResult.getString(6);

                if(email.equals(Email) || username.equals(Username))
                {
                    throw  new InvalidCredentialsRegistration("User already exists");

                }
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
            e.getCause();
            System.out.println("Querying user events failed!");
        }
        return true;
    }
}
