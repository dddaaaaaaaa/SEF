package domain;
import java.sql.*;

public class DatabaseConnection extends DatabaseCredentials{
    public Connection databaseLink;

    public Connection getConnection(){
        try{
            databaseLink = DriverManager.getConnection(super.url,super.databaseName, super.databasePassword);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
