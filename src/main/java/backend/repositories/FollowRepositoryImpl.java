package backend.repositories;

import backend.configuration.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FollowRepositoryImpl implements FollowRepository {

    public boolean follow(long user, long user_to_follow) {
        String sql = "SELECT COUNT(*)FROM y.Followers WHERE FollowerID= ? AND FollowingID = ? ";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user);

            preparedStatement.setLong(2, user_to_follow);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createFollower(long user, long user_to_follow) {
        String sql = "INSERT INTO y.Followers (FollowerID, FollowingID) values (?,?)";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user);

            preparedStatement.setLong(2, user_to_follow);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFollower(long user, long user_to_follow) {
        String sql = "DELETE FROM y.Followers WHERE FollowerID = ? AND FollowingID = ?";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user);

            preparedStatement.setLong(2, user_to_follow);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getFollowing(long userId) {
        List<String> usernames = new ArrayList<>();
        String sql = "{call y.getFollowing(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, userId);

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                while (resultSet.next()) {
                    usernames.add(resultSet.getString("username"));
                }
            }
            return usernames;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
    }

    public List<String> getFollowers(long userId) {
        List<String> usernames = new ArrayList<>();
        String sql = "{call y.getFollowers(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, userId);

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                while (resultSet.next()) {
                    usernames.add(resultSet.getString("username"));
                }
            }
            return usernames;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
    }

}
