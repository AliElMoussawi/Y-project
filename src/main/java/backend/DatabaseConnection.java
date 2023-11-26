package backend;

import backend.models.AuthenticationDTO;

import java.sql.*;

public class DatabaseConnection {
    public static Connection connect=createConnection();

    public static Connection createConnection() {
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

    public DatabaseConnection(){
        connect=createConnection();
    }
    public boolean db_Register(String username, String password){

        String sql = "INSERT INTO y.Yapper (username, password) " +
                "VALUES (?,?)";

        try {
            assert connect != null;
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,password);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted.");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void db_Follow(String user1, String user2){
        String sql = "INSERT INTO y.relationship (follower_id, following_id) " +
                "VALUES (?,?)";

        try {
            assert connect != null;
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1,user1);
                preparedStatement.setString(2,user2);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean db_exists(String username){
        String sql = "SELECT COUNT(*) FROM y.Yapper WHERE username = ?";
        try {
            assert connect != null;
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1,username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);

                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public boolean db_follows(String user, String user_to_follow){
        String sql = "SELECT COUNT(*) FROM y.relationship WHERE column1 = ? AND column2 = ?";
        try {
            assert connect != null;
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1,user);
                preparedStatement.setString(2,user_to_follow);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);

                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

