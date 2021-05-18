package domain;

import exceptions.InvalidCredentialsRegistration;

import java.sql.*;
import java.util.Date;

public class Register {
    //public String firstName, lastName, email, username;

    public boolean registerUser(User user) throws InvalidCredentialsRegistration {
        try {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("SELECT count(1) FROM \"user\" WHERE username = ?;");
            ps.setString(1, user.getUsername());
            ResultSet queryResult = ps.executeQuery();

            //System.out.println("Registering " + user.getUsername());
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    //user already exists
                    System.out.println("User already exists!");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
            return false;
        }
        return true;
    }
}
