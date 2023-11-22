package backend.middleware;

import backend.controllers.AuthenticationController;
import backend.interfaces.Controller;
import backend.models.RequestObject;
import backend.models.ResponseObject;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class RequestRouter {
    private final Map<Action, Controller> controllers;
    private final Interceptor interceptor;
    private static RequestRouter instance = null;
    public static synchronized RequestRouter getInstance() {
        if (instance == null) {
            instance = new RequestRouter();
        }
        return instance;
    }
    public RequestRouter() {
        this.interceptor = new Interceptor();
        this.controllers = new HashMap<>();
        // Initialize controllers for each action
        controllers.put(Action.LOGIN, new AuthenticationController());
        controllers.put(Action.SIGNUP, new AuthenticationController());

    }

    public ResponseObject routeRequest(RequestObject request) {
        if (!interceptor.validRequest(request)) {
            return new ResponseObject(StatusCode.UNAUTHORIZED, null, "Invalid token or request");
        }

        Action action = request.getAction();
        Controller controller = controllers.get(action);

        if (controller != null) {
            return controller.handleRequest(request);
        } else {
            return new ResponseObject(StatusCode.BAD_REQUEST, null,"Unknown action");
        }
    }


}