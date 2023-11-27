package backend.models.protocol;

import backend.utils.enums.StatusCode;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @NonNull
    private StatusCode statusCode;
    private Object object;
    @NonNull
    private String message;
    private String requestId;

    public ResponseObject(@NotNull StatusCode statusCode, Object object,@NotNull String message) {
        this.statusCode = statusCode;
        this.object = object;
        this.message = message;
        this.requestId = null;
    }
}
