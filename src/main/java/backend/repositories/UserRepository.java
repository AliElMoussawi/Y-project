package backend.repositories;

import backend.repositories.models.database.User;

public interface UserRepository {
    User findById(long id);
    User findByUsernameOrEmail(String usernameOrEmail);
    boolean save(User user);

}
