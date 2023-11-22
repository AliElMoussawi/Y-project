package backend.models;

import backend.utils.enums.Action;
import backend.utils.enums.Method;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private Action action;
    private Object object;
    private long timeOfCreation;
    private String token;
    private Method method;

}
