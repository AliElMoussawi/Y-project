package backend.models;

import backend.utils.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
public class RequestObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private Action action;
    private Object object;
    private long timeOfCreation;
    private String token;
    private String method;

}
