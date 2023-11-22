package backend.interfaces;

import backend.models.RequestObject;
import backend.models.ResponseObject;

public interface Controller {
    ResponseObject handleRequest(RequestObject request);
}
