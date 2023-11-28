package backend.interfaces;

import backend.Server;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;

public interface Controller {
    ResponseObject handleRequest(RequestObject request, Server.ClientHandler clientHandler);
}
