package backend.security;

import java.util.*;

public class UserCredentialsManager {
    private final Map<String, String> userCredentials;
    private static UserCredentialsManager instance = null;
    private static Token token;
    public UserCredentialsManager() {
        userCredentials = new HashMap<>();
        token = Token.getInstance();
    }

    public static synchronized UserCredentialsManager getInstance() {
        if (instance == null) {
            instance = new UserCredentialsManager();
        }
        return instance;
    }
    public boolean isUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    public boolean register(String username, String password) {
        if (userCredentials.containsKey(username)) {
            return false;
        }
        userCredentials.put(username, password);
        return true;
    }

    public String authenticate(String username, String password) throws Exception {
        if (isUser(username, password)){
            return token.createToken(username);
        }
        throw new Exception("User doesn't exist");
    }
    public String signUp(String username, String password) throws Exception {
            if (register(username, password)){
                return token.createToken(username);
            }
        throw new Exception("User already exist");

    }
}
