package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long UserId;
    private long postId;
    private String content;
}
