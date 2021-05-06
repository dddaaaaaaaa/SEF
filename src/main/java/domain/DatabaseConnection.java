package domain;
import java.sql.*;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName="remindme"; //personal database name needed
        String databaseUser="root"; //personal database user needed
        String databasePassword="raluca123"; // personal database password needed
        String url="jdbc:mysql://localhost:3306/"+ databaseName; // check your local host url

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser, databasePassword);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
