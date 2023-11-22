package backend;

import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter out;

    public Client(String address, int port) {

        try {
            socket = new Socket(address, port);
            System.out.println("Connected to the chat server");

            input = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new ReadThread(socket)).start();

            new Thread(() -> {
                try {
                    while (true) {
                        out.println("HEARTBEAT");
                        Thread.sleep(2000); // Send heartbeat every 2 seconds
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            String userInput;
            while ((userInput = input.readLine()) != null) {
                out.println(userInput);
            }
        } catch (UnknownHostException e) {
            System.err.println("Host unknown: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    private static class ReadThread implements Runnable {
        private BufferedReader reader;

        public ReadThread(Socket socket) throws IOException {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                String response;
                while ((response = reader.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }
    public void sendMessage(String msg) {
        out.println(msg);
    }

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 9991;

        new Client(host, port);
    }

}
