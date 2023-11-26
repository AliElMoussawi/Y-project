package backend.repositories;

import backend.configuration.DatabaseConnection;
import backend.models.database.User;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository {

    public User findById(long id) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT * FROM y.Users WHERE username = ? OR email = ?";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usernameOrEmail);
            preparedStatement.setString(2, usernameOrEmail);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(User user) {
        if (user.getId() == null) {
            return insertUser(user);
        } else {
            return updateUser(user);
        }
    }

    private boolean insertUser(User user) {
        String sql = "INSERT INTO y.Users (username, email, password, fullName, bio, location, website, profilePictureUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setPreparedStatement(preparedStatement, user);

            int affectedRows = preparedStatement.executeUpdate();
            handleGeneratedKeys(user, preparedStatement, affectedRows);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateUser(User user) {
        String sql = "UPDATE y.Users SET username = ?, email = ?, password = ?, fullName = ?, bio = ?, location = ?, website = ?, profilePictureUrl = ? WHERE id = ?";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setPreparedStatement(preparedStatement, user);
            preparedStatement.setLong(9, user.getId());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setPreparedStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getFullName());
        preparedStatement.setString(5, user.getBio());
        preparedStatement.setString(6, user.getLocation());
        preparedStatement.setString(7, user.getWebsite());
        preparedStatement.setString(8, user.getProfilePictureUrl());
    }

    private void handleGeneratedKeys(User user, PreparedStatement preparedStatement, int affectedRows) throws SQLException {
        if (affectedRows > 0) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("UserID"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setFullName(resultSet.getString("fullName"));
        user.setBio(resultSet.getString("bio"));
        user.setLocation(resultSet.getString("location"));
        user.setWebsite(resultSet.getString("website"));
        user.setProfilePictureUrl(resultSet.getString("profilePictureUrl"));

        return user;
    }


}
