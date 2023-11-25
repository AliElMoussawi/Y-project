package backend.repositories;

import backend.models.database.User;

public interface UserRepository {
    public User findById(long id);
    void save(User user);

}
