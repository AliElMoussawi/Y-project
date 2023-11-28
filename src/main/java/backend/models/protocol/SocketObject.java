package backend.models.protocol;

import backend.utils.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocketObject implements Serializable {
    private Action action;
    private Object object;
    private long timeOfCreation;
}
