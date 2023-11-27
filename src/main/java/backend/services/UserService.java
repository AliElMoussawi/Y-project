package backend.services;

import backend.repositories.models.database.User;

public interface UserService {
    User getUserById(int id);
    User getUserByUsernameOrEmail(String usernameOrEmail);
    String createUser(User user) throws Exception;
    String authenticate(String usernameOrEmail, String passowrd) throws Exception;
}
