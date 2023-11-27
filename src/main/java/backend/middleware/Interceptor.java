package backend.middleware;

import backend.security.Token;
import backend.repositories.models.protocol.RequestObject;
import backend.utils.enums.Action;
import org.jetbrains.annotations.NotNull;

public class Interceptor {
    private final Token token;

    public Interceptor() {
        token = Token.getInstance();
    }

    public boolean validRequest(@NotNull RequestObject request) {
        Action action = request.getAction();
        if (action.requiresToken()) {
            return token.validateToken(request.getToken());
        }
        return true;
    }

}
