package backend;

import backend.models.ResponseObject;

import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private ObjectInputStream objectIn;
    private BufferedReader input;

    private ObjectOutputStream objectOut;
    public Client(String address, int port) {

        try {
            socket = new Socket(address, port);
            System.out.println("Connected to the chat server");

            OutputStream outputStream = socket.getOutputStream();

            objectOut = new ObjectOutputStream(outputStream);

            objectIn = new ObjectInputStream(socket.getInputStream());

            input = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Sending messages to the ServerSocket");

            objectOut.writeObject("messages");

            objectOut.flush();

            new Thread(new ReadThread()).start();

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    private class ReadThread implements Runnable {
        public void run() {
            try {
                Object response;
                while ((response = objectIn.readObject()) != null) {
                    System.out.println("response: "+response+" response type:"+response.getClass());
                    if (response instanceof ResponseObject) {
                        ResponseObject responseObject = ((ResponseObject) response);
                        // just for checking
                        System.out.println("Received: " + responseObject.getStatusCode() +" object:  "+((responseObject.getObject()==null)? " null ": responseObject.getObject())+" "+ responseObject.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }
    public void sendObject(Object msg) {
        try {
            objectOut.writeObject(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 9991;

        new Client(host, port);
    }
}
