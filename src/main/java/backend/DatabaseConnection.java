package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public Connection getConnection() {
        String jdbcUrl = "jdbc:sqlserver://sportshivedbserver.database.windows.net:1433;database=sportshive;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;";
        String username = "Moustapha";
        String password = System.getenv("dbPassword"); //OR use the password that Bdeir gave you

        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        }
        catch (Exception e){
            return null;
        }
    }

}

