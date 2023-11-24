package backend.models;

import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ResponseObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private StatusCode statusCode;
    private Object object;
    private String message;

}
