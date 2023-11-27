package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class PostDTO implements Serializable {
    private int yapId;
    private int userId;
    private String username;
    private String Content;
    private Instant timestamp;


    public PostDTO() {

    }
}
