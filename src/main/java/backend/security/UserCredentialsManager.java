package backend.security;

import backend.DatabaseConnection;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class UserCredentialsManager {
    private final Map<String, String> userCredentials;
    private static UserCredentialsManager instance = null;

    static DatabaseConnection DB_connection=new DatabaseConnection();
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

    public String hashPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            return enc.encodeToString(salt)+enc.encodeToString(hash);
        }
        catch (NoSuchAlgorithmException e){
            System.out.print("No Such alogirhtm, more info: "+e.getMessage());
            return null;
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean register(String username, String password)  {
        if (userCredentials.containsKey(username)) {
            return false;
        }


        String pass=hashPassword(password);
        if(pass==null)
            throw new RuntimeException("An error occurred with password hashing");

        return DB_connection.db_Register(username,pass);


        //userCredentials.put(username, password);
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
