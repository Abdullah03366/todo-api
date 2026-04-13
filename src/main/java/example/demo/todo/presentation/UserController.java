package example.demo.todo.presentation;

import example.demo.todo.application.UserService;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.presentation.dto.DTOMapper;
import example.demo.todo.presentation.dto.UserDTO;
import example.demo.todo.security.AuthRequestContext;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDTO getCurrent(HttpServletRequest request) {
        var principal = AuthRequestContext.requireUser(request);
        return DTOMapper.toUserDTO(userService.findById(principal.getUserId()));
    }

    @PatchMapping("/me")
    public UserDTO updateCurrent(HttpServletRequest authRequest, @RequestBody UpdateUserDTO request)
            throws InvalidUsernameException {
        var principal = AuthRequestContext.requireUser(authRequest);
        return DTOMapper.toUserDTO(userService.update(principal.getUserId(), request.username(), request.password()));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrent(HttpServletRequest request) {
        var principal = AuthRequestContext.requireUser(request);
        userService.delete(principal.getUserId());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoSuchElementException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({InvalidUsernameException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(Exception ex) {
        return ex.getMessage();
    }

    public record UpdateUserDTO(String username, String password) {}
}
