package backend.repositories;

import backend.configuration.DatabaseConnection;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {

    @Override
    public boolean createComment(long postId, long userId, String content) {
        return createCommentHelper(postId, userId, content) && incrementReplyCount(postId);
    }

    private boolean createCommentHelper(long postId, long userId, String content) {
        String sql = "INSERT INTO y.Replies (YapID,UserID,Content,Timestamp) VALUES (?, ?, ?, ? )";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, postId);

            preparedStatement.setLong(2, userId);
            preparedStatement.setString(3, content);
            preparedStatement.setTimestamp(4, Timestamp.from(Instant.now()));
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean incrementReplyCount(long postId) {
        String sql = "UPDATE y.Yaps  SET ReplyCount = ReplyCount + 1 WHERE YapID = ?";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, postId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean removeComment(long postId, long userId, String content) {
        return removeCommentHelper(postId,userId,content)&&decrementReplyCount(postId);
    }
    public boolean removeCommentHelper(long postId, long userId, String content) {
        String sql = "DELETE FROM y.Replies WHERE YapID=? AND UserID = ? AND Content= ?";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, postId);

            preparedStatement.setLong(2, userId);
            preparedStatement.setString(3, content);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean decrementReplyCount(long postId) {
        String sql = "UPDATE y.Yaps  SET ReplyCount = ReplyCount - 1  WHERE YapID = ?";
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, postId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
