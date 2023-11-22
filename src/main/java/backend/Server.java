package backend;

import backend.middleware.RequestRouter;
import backend.models.RequestObject;
import backend.models.ResponseObject;
import backend.security.UserCredentialsManager;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int PORT = 9991;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws IOException {
        socketServer();

    }
    private static void socketServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started. Listening on Port " + PORT);
        schedulePeriodicMessage();
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket);
                clients.add(clientThread);
                new Thread(clientThread).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static void schedulePeriodicMessage() {

        Runnable sendMessageTask = () -> {
            System.out.println("server sent the periodic message");

            broadcastMessage("Server: This is a periodic message.", null);
        };

        // Schedule the task to run every 10 seconds
        scheduler.scheduleAtFixedRate(sendMessageTask, 0, 10, TimeUnit.SECONDS);
    }

    public static void broadcastMessage(String message, ClientHandler excludeUser) {
        for (ClientHandler aClient : clients) {
            if (aClient != excludeUser && aClient.isActive()) {
                aClient.sendMessage(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;
        private volatile boolean isActive = true;
        private UserCredentialsManager credentialsManager;
        private  RequestRouter requestRouter;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.credentialsManager = UserCredentialsManager.getInstance();
            this.objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
            this.objectOut.flush();
            this.objectIn = new ObjectInputStream(clientSocket.getInputStream());
            this.requestRouter = RequestRouter.getInstance();

        }

        public void run() {
            try {
                while (isActive) {
                    Object obj = objectIn.readObject();
                    if (obj instanceof RequestObject) {
                        RequestObject request = (RequestObject) obj;
                        ResponseObject requestResponse = requestRouter.routeRequest(request);
                        objectOut.writeObject(requestResponse);
                        objectOut.flush();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                // Exception handling
            } finally {
                cleanUp();
            }
        }
        private void cleanUp() {
            try {
                if (objectOut != null) objectOut.close();
                if (objectIn != null) objectIn.close();
                if (clientSocket != null) clientSocket.close();
                clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public boolean isActive() {
            return isActive;
        }

        void sendMessage(Object message) {
            try {
                objectOut.writeObject(message);
                objectOut.flush();
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
                isActive = false;
            }
        }

    }
}
