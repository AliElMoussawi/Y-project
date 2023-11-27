package backend.repositories;

import backend.configuration.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostRepositoryImpl implements PostRepository{
    public boolean createPost(long userId, String comment) {
        String sql = "{call y.spCreatePost(?, ?)}";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, userId);
            callableStatement.setString(2, comment);

            int rowsAffected = callableStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
