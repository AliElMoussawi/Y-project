package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class EditPostDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long yapId;
    private String content;
}
