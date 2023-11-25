package backend.services;

import backend.models.database.User;

public interface UserService {
    User getUserById(int id);
    void createUser(User user);

}
