package backend.repositories;

import backend.configuration.DatabaseConnection;
import backend.dto.PostDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostRepositoryImpl implements PostRepository {

    public PostDTO findById(long id) {
        String sql = "SELECT * FROM Posts WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPostFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PostDTO> findByUserId(long userId) {
        String sql = "SELECT * FROM Posts WHERE userId = ?";
        List<PostDTO> posts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(extractPostFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }


    private boolean insertPost(PostDTO post) {
        String sql = "INSERT INTO Posts (userId, content, timestamp) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setPostPreparedStatement(preparedStatement, post);

            int affectedRows = preparedStatement.executeUpdate();
            handleGeneratedKeys(post, preparedStatement, affectedRows);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updatePost(PostDTO post) {
        String sql = "UPDATE Posts SET userId = ?, content = ?, timestamp = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setPostPreparedStatement(preparedStatement, post);
            preparedStatement.setLong(4, post.getId());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<PostDTO> findPostsForFollowers(long userId) {
        // Assuming you have a FollowersRepository to manage followers
        FollowersRepository followersRepository = new FollowersRepositoryImpl();
        List<Long> followerIds = followersRepository.findFollowerIds(userId);

        // Fetch posts for the user and their followers
        String sql = "SELECT * FROM Posts WHERE userId IN (" + String.join(",", Collections.nCopies(followerIds.size(), "?")) + ") ORDER BY timestamp DESC";
        List<PostDTO> posts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < followerIds.size(); i++) {
                preparedStatement.setLong(i + 1, followerIds.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(extractPostFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }



    private void setPostPreparedStatement(PreparedStatement preparedStatement, PostDTO post) throws SQLException {
        preparedStatement.setLong(1, post.getUserId());
        preparedStatement.setString(2, post.getContent());
        preparedStatement.setTimestamp(3, Timestamp.from(post.getTimestamp()));
    }

    private void handleGeneratedKeys(PostDTO post, PreparedStatement preparedStatement, int affectedRows) throws SQLException {
        if (affectedRows > 0) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private PostDTO extractPostFromResultSet(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("id"));
        post.setUserId(resultSet.getLong("userId"));
        post.setContent(resultSet.getString("content"));
        post.setTimestamp(resultSet.getTimestamp("timestamp").toInstant());

        return post;
    }
}
