package example.demo.todo.application;

import example.demo.todo.data.UserRepository;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.domain.user.Username;
import example.demo.todo.security.PasswordHasher;
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

    public User findByUsername(String username) {
        return userRepository.findByUsername_Name(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
    }

    public User create(String username, String plainPassword) throws InvalidUsernameException {
        validatePassword(plainPassword);
        validateUsernameUnique(username, null);
        User user = new User(username, PasswordHasher.hash(plainPassword));
        return userRepository.save(user);
    }

    public User update(UUID id, String username, String plainPassword) throws InvalidUsernameException {
        User user = findById(id);
        validateUsernameUnique(username, id);
        user.setUsername(new Username(username));
        if (plainPassword != null && !plainPassword.isBlank()) {
            validatePassword(plainPassword);
            user.setPasswordHash(PasswordHasher.hash(plainPassword));
        }
        return userRepository.save(user);
    }

    public User authenticate(String username, String plainPassword) {
        validatePassword(plainPassword);
        User user = findByUsername(username);
        if (!PasswordHasher.matches(plainPassword, user.getPasswordHash())) {
            throw new NoSuchElementException("Invalid credentials");
        }
        return user;
    }

    private void validatePassword(String plainPassword) {
        if (plainPassword == null || plainPassword.length() < 8 || plainPassword.length() > 72) {
            throw new IllegalArgumentException("Password must be between 8 and 72 characters");
        }
    }

    private void validateUsernameUnique(String username, UUID currentUserId) {
        userRepository.findByUsername_Name(username).ifPresent(existingUser -> {
            if (currentUserId == null || !currentUserId.equals(existingUser.getId())) {
                throw new IllegalArgumentException("Username already exists");
            }
        });
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
