package backend.interfaces;

import backend.models.ResponseObject;

public interface Command {
    ResponseObject execute();
}