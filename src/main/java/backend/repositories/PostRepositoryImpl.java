package backend.repositories;

import backend.configuration.DatabaseConnection;
import backend.dto.PostDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostRepositoryImpl implements PostRepository {

    public PostDTO getPostById(int id) {
        String sql = "SELECT * FROM y.Yaps WHERE yapId = ?";
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

    public List<PostDTO> getPostByUserId(int userId) {
        String sql = "SELECT * FROM y.Yaps WHERE userId = ?";
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

    @Override
    public List<PostDTO> getPostsForFollowers(int userId) {
        return null;
    }


    public boolean insertPost(PostDTO post) {
        String sql = "INSERT INTO y.Yaps (userId, content, timestamp) VALUES (?, ?, ?)";
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

    public boolean updatePost(PostDTO post) {
        String sql = "UPDATE y.Yaps SET userId = ?, content = ?, timestamp = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setPostPreparedStatement(preparedStatement, post);
            preparedStatement.setLong(4, post.getYapId());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deletePost(PostDTO post) {
        if (post.getYapId() == 0) {
            System.out.println("Cannot delete post without ID.");
            return false;
        }

        String sql = "DELETE FROM Posts WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, post.getYapId());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
//    public List<PostDTO> findPostsForFollowers(long userId) {
//        FollowersRepository followersRepository = new FollowersRepositoryImpl();
//        List<Long> followerIds = followersRepository.findFollowerIds(userId);
//
//        // Fetch posts for the user and their followers
//        String sql = "SELECT * FROM Posts WHERE userId IN (" + String.join(",", Collections.nCopies(followerIds.size(), "?")) + ") ORDER BY timestamp DESC";
//        List<PostDTO> posts = new ArrayList<>();
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            for (int i = 0; i < followerIds.size(); i++) {
//                preparedStatement.setLong(i + 1, followerIds.get(i));
//            }
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    posts.add(extractPostFromResultSet(resultSet));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return posts;
//    }

    private void setPostPreparedStatement(PreparedStatement preparedStatement, PostDTO post) throws SQLException {
        preparedStatement.setLong(1, post.getUserId());
        preparedStatement.setString(2, post.getContent());
        preparedStatement.setTimestamp(3, Timestamp.from(post.getTimestamp()));
    }

    private void handleGeneratedKeys(PostDTO post, PreparedStatement preparedStatement, int affectedRows) throws SQLException {
        if (affectedRows > 0) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setYapId((int) generatedKeys.getLong(1));
                }
            }
        }
    }

    private PostDTO extractPostFromResultSet(ResultSet resultSet) throws SQLException {
        PostDTO post = new PostDTO();
        post.setYapId((int) resultSet.getLong("id"));
        post.setUserId((int) resultSet.getLong("userId"));
        post.setContent(resultSet.getString("content"));
        post.setTimestamp(resultSet.getTimestamp("timestamp").toInstant());

        return post;
    }
}
