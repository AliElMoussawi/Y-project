package backend;

import backend.middleware.RequestRouter;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.utils.enums.StatusCode;


import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int PORT = 9991;
    public static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


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

    public static void broadcastMessage(Object message, ClientHandler excludeUser) {
        for (ClientHandler aClient : clients) {
            if (aClient != excludeUser && aClient.isActive()) {
                aClient.sendMessage(message);
            }
        }
    }

    public static class ClientHandler implements Runnable {
        public final Socket clientSocket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;
        private volatile boolean isActive = true;
        private  RequestRouter requestRouter;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
            InputStream inputStream = socket.getInputStream();
            this.objectIn = new ObjectInputStream(inputStream);
            this.requestRouter = RequestRouter.getInstance();
            System.out.println("new Client connected:" +socket.getInetAddress() + " "+ socket.getChannel());
        }

        public void run() {
            try {
                while (isActive) {
                    Object obj = objectIn.readObject();
                    System.out.println("recieved object from the client : "+ obj.toString());
                    if (obj instanceof RequestObject) {
                        RequestObject request = (RequestObject) obj;
                        ResponseObject requestResponse = requestRouter.routeRequest(request, this);
                        requestResponse.setRequestId(request.getId());
                        objectOut.writeObject(requestResponse);
                        objectOut.flush();
                    }
                    else {
                        System.out.println("Bad request invalid payload");
                        ResponseObject requestResponse = new ResponseObject(StatusCode.BAD_REQUEST, null, "Bad invalid");
                        objectOut.writeObject(requestResponse);
                        objectOut.flush();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                // todo handle this error
            } catch (Exception e) {
                throw new RuntimeException(e);
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
    public static void main(String[] args) throws IOException {
//        UserServiceImpl userService = new UserServiceImpl();
//        User user = new User();
//        user.setFullName("John Smith1");
//        user.setUsername("johnSmith1");
//        user.setEmail("johnsmith1@gmail.com");
//        userService.createUser(user);
        socketServer();
    }
}
