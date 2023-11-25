package backend.configuration;

import java.sql.*;

public class DatabaseConnection {
    public static java.sql.Connection getConnection() {
        String userName = "AliMoussawi";
        String password = "abd44fcb-0d7a-4434-9754-5a9d2d3d8d17";
        String connectionUrl = "jdbc:sqlserver://sportshivedbserver.database.windows.net:1433;database=sportshive;";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
                return DriverManager.getConnection(connectionUrl, userName, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

}