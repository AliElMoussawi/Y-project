package backend.repositories;

import backend.configuration.DatabaseConnection;
import backend.models.database.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    @Override
    public List<Post> getPosts(long userId) {
        List<Post> posts = new ArrayList<>();
        String sql = "{call y.spGetPosts(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setLong(1, userId);

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post();
                    post.setYapId(resultSet.getInt("yapId"));
                    post.setUserId(resultSet.getInt("userId"));
                    post.setContent(resultSet.getString("content"));
                    post.setTimeStamp(resultSet.getTimestamp("timeStamp"));
                    post.setRepostCount(resultSet.getInt("repostCount"));
                    post.setLikeCount(resultSet.getInt("likeCount"));
                    post.setReplyCount(resultSet.getInt("replyCount"));
                    posts.add(post);
                }
            }

            return posts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
