package example.demo.todo.presentation;

import example.demo.todo.application.UserService;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.presentation.dto.DTOMapper;
import example.demo.todo.presentation.dto.UserDTO;
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
    public List<UserDTO> getAll() {
        return userService.findAll().stream()
                .map(DTOMapper::toUserDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable UUID id) {
        return DTOMapper.toUserDTO(userService.findById(id));
    }

    @GetMapping("/login")
    public UserDTO getByUsername(@RequestParam String username) {
        return DTOMapper.toUserDTO(userService.findByUsername(username));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody CreateUserDTO request) throws InvalidUsernameException {
        return DTOMapper.toUserDTO(userService.create(request.username()));
    }

    @PatchMapping("/{id}")
    public UserDTO update(@PathVariable UUID id, @RequestBody UpdateUserDTO request) throws InvalidUsernameException {
        return DTOMapper.toUserDTO(userService.update(id, request.username()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
