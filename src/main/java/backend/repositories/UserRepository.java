package backend.repositories;

import backend.models.database.User;

public interface UserRepository {
    User findById(long id);
    User findByUsernameOrEmail(String usernameOrEmail);
    boolean save(User user);

}
