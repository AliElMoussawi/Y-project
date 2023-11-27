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
    public boolean editPost(long yapId, String comment) {
        String sql = "{call y.spEditPost(?, ?)}";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, yapId);
            callableStatement.setString(2, comment);

            int rowsAffected = callableStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removePost(long yapId) {
        String sql = "{call y.spRemovePost(?)}";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, yapId);
            int rowsAffected = callableStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean likePost(long userId, long yapId){
        String sql = "{call y.spLikePost(?,?)}";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, userId);
            callableStatement.setLong(2, yapId);

            int rowsAffected = callableStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unlikePost(long userId, long yapId){
        String sql = "{call y.spRemoveLike(?,?)}";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, userId);
            callableStatement.setLong(2, yapId);

            int rowsAffected = callableStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
