package backend.repositories.models.protocol;

import backend.utils.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocketObject {
    private Action action;
    private Object object;
    private long timeOfCreation;
}
