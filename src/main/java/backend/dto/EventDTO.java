package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class EventDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long followerId;
    private long  toFollowId;
}