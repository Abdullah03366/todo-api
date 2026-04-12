package example.demo.todo.application;

import example.demo.todo.data.UserRepository;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.domain.user.Username;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }

    public User create(String username) throws InvalidUsernameException {
        User user = new User(username);
        return userRepository.save(user);
    }

    public User update(UUID id, String username) throws InvalidUsernameException {
        User user = findById(id);
        user.setUsername(new Username(username));
        return userRepository.save(user);
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
