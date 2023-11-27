package backend.interfaces;

import backend.repositories.models.protocol.RequestObject;
import backend.repositories.models.protocol.ResponseObject;

public interface Controller {
    ResponseObject handleRequest(RequestObject request);
}
