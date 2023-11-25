package backend.services;

import backend.models.database.User;
import backend.repositories.UserRepository;
import backend.repositories.UserRepositoryImpl;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();

    public UserServiceImpl() {
    }

    public User getUserById(int id) {
        return userRepository.findById(id);

    }

    public void createUser(User user) {
        userRepository.save(user);
    }

}