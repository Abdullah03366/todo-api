package example.demo.todo.application;

import example.demo.todo.data.TodoListRepository;
import example.demo.todo.data.UserRepository;
import example.demo.todo.domain.TodoList;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.domain.user.Username;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TodoListRepository todoListRepository;

    public UserService(UserRepository userRepository, TodoListRepository todoListRepository) {
        this.userRepository = userRepository;
        this.todoListRepository = todoListRepository;
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

    @Transactional
    public User addTodoList(UUID userId, UUID todoListId) {
        User user = findById(userId);
        TodoList todoList = todoListRepository.findById(todoListId)
                .orElseThrow(() -> new NoSuchElementException("TodoList not found: " + todoListId));

        boolean alreadyLinked = user.getTodoLists().stream()
                .anyMatch(existingTodoList -> existingTodoList.getId().equals(todoListId));
        if (!alreadyLinked) {
            user.addTodoList(todoList);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User removeTodoList(UUID userId, UUID todoListId) {
        User user = findById(userId);

        TodoList linkedTodoList = user.getTodoLists().stream()
                .filter(existingTodoList -> existingTodoList.getId().equals(todoListId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "TodoList " + todoListId + " is not linked to User " + userId));

        user.removeTodoList(linkedTodoList);
        return userRepository.save(user);
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
