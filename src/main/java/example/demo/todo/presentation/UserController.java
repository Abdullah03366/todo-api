package example.demo.todo.presentation;

import example.demo.todo.application.UserService;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody CreateUserDTO request) throws InvalidUsernameException {
        return userService.create(request.username());
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody UpdateUserDTO request) throws InvalidUsernameException {
        return userService.update(id, request.username());
    }

    @PutMapping("/{userId}/todolists/{todoListId}")
    public User addTodoList(@PathVariable UUID userId, @PathVariable UUID todoListId) {
        return userService.addTodoList(userId, todoListId);
    }

    @DeleteMapping("/{userId}/todolists/{todoListId}")
    public User removeTodoList(@PathVariable UUID userId, @PathVariable UUID todoListId) {
        return userService.removeTodoList(userId, todoListId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoSuchElementException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(Exception ex) {
        return ex.getMessage();
    }

    public record CreateUserDTO(String username) {}
    public record UpdateUserDTO(String username) {}
}
