package backend.models.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
public class Post  implements Serializable {
    private static final long serialVersionUID = 1L;

    private int yapId;
    private int userId;
    private String content;
    private Timestamp timeStamp;
    private int repostCount;
    private int likeCount;
    private int replyCount;


    public Post() {
    }

    @Override
    public String toString() {
        return "Post{" +
                "yapId=" + yapId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", timeStamp=" + timeStamp +
                ", repostCount=" + repostCount +
                ", likeCount=" + likeCount +
                ", replyCount=" + replyCount +
                '}';
    }
}
