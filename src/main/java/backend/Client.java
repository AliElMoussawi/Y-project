package backend;

import backend.models.AuthenticationDTO;
import backend.models.FollowClientDTO;
import backend.models.RequestObject;
import backend.models.ResponseObject;
import backend.utils.enums.Action;
import backend.utils.enums.Method;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

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

            Scanner scan=new Scanner(System.in);

            System.out.print("Enter user credentials");
            String user1=scan.next();
            String pass1=scan.next();



            objectOut.writeObject(new RequestObject(Action.SIGNUP,new AuthenticationDTO(user1,pass1),(long)22,"token",null));

            objectOut.flush();

//            if(!pass1.equals("pass1"))
//                objectOut.writeObject(new RequestObject(Action.FOLLOW,new FollowClientDTO(new AuthenticationDTO(user1,pass1),"user3use"),(long)22,"token",null));

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
                        ResponseObject responseObject = (ResponseObject) response;
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

        String host = "localhost";
        int port = 9991;

        new Client(host, port);
       // new Client(host, port);

    }
}
