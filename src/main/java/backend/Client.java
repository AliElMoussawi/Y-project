package backend;

import backend.dto.*;
import backend.models.protocol.ResponseObject;
import backend.models.protocol.RequestObject;
import backend.models.protocol.SocketObject;
import backend.utils.enums.Action;
import backend.utils.enums.Method;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
    private ObjectInputStream objectIn;
    private Map<String, Consumer<ResponseObject>> responseHandlers;

    private ObjectOutputStream objectOut;
    public Client(String address, int port, int clientNumber) {
        responseHandlers = new HashMap<>();

        try {
            System.out.println("create client " +clientNumber );

            socket = new Socket(address, port);

            System.out.println("Connected to the chat server");

            OutputStream outputStream = socket.getOutputStream();

            objectOut = new ObjectOutputStream(outputStream);

            objectIn = new ObjectInputStream(socket.getInputStream());

            new Thread(new ReadThread()).start();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    private class ReadThread implements Runnable {
        public void run() {
            try {
                Object receivedObject;
                while ((receivedObject = objectIn.readObject()) != null) {
                    System.out.println("response: "+receivedObject+" response type:"+receivedObject.getClass());

                    if (receivedObject instanceof ResponseObject) {
                        processResponseObject((ResponseObject) receivedObject);
                        System.out.println("Received: " + ((ResponseObject) receivedObject).getStatusCode() +" object:  "+((((ResponseObject) receivedObject).getObject() == null)? " null ": ((ResponseObject) receivedObject).getObject())+" "+ ((ResponseObject) receivedObject).getMessage());
                    } else if (receivedObject instanceof SocketObject) {
                        processSocketObject((SocketObject) receivedObject);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }

    private void processResponseObject(@NotNull ResponseObject responseObject) {
        String requestId = responseObject.getRequestId();
        if (responseHandlers.containsKey(requestId)) {
            responseHandlers.get(requestId).accept(responseObject);
            responseHandlers.remove(requestId);
        }
    }
    private void processSocketObject(SocketObject socketObject) {
        // todo  broadcast
    }

     private void sendObject(Object object) {
        try {
            objectOut.writeObject(object);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }

    private Future<ResponseObject> sendRequest(RequestObject request) {
        CompletableFuture<ResponseObject> responseFuture = new CompletableFuture<>();
        String requestId = UUID.randomUUID().toString();
        responseHandlers.put(requestId, responseFuture::complete);
        request.setId(requestId);
        sendObject(request);
        return responseFuture;
    }

    public static ResponseObject createRequest(@NotNull Client client, RequestObject request) {
        Future<ResponseObject> responseFuture = client.sendRequest(request);
        try {
            ResponseObject responseObject  = responseFuture.get();
            System.out.println("Response received: " + responseObject.getMessage());
            return responseObject;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9991;
        Client client = new Client(host, port, 1);

        RequestObject request = new RequestObject();
        //AuthenticationDTO authenticationDTO=new AuthenticationDTO("jakepaul13","jake@prime.com","jake@123");
        //EventDTO authenticationDTO = new EventDTO(1L,-1);
        //EditPostDTO postDTO = new EditPostDTO(2, "Hello World from Hanin 600");
        LikeDTO postDTO = new LikeDTO(1,5);
        request.setObject(postDTO);
        request.setMethod(Method.POST);
        request.setAction(Action.LIKE_POST);
        //request.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYWtlcGF1bDEzIiwiaWF0IjoxNzAxMDIxMTU1LCJleHAiOjE3MDEwMjQ3NTV9.aKg1bokiarJ0H_YNQoOUZO6Gf9rWz772O8gOVfLaHYicsx_imkJyQXKrMAE2fChvCRFm5DtWj0eqSP_37Ecyyg");
        ResponseObject object = createRequest(client, request);

    }
}
