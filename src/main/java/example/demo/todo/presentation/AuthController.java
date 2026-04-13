package example.demo.todo.presentation;

import example.demo.todo.application.UserService;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.presentation.dto.DTOMapper;
import example.demo.todo.presentation.dto.UserDTO;
import example.demo.todo.security.AppUserPrincipal;
import example.demo.todo.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody AuthRequest request) throws InvalidUsernameException {
        User user = userService.create(request.username(), request.password());
        String token = jwtService.generateToken(
                new AppUserPrincipal(user.getId(), user.getUsername().getName(), user.getPasswordHash())
        );
        return new AuthResponse(token, DTOMapper.toUserDTO(user));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userService.authenticate(request.username(), request.password());
        String token = jwtService.generateToken(
                new AppUserPrincipal(user.getId(), user.getUsername().getName(), user.getPasswordHash())
        );
        return new AuthResponse(token, DTOMapper.toUserDTO(user));
    }

    @ExceptionHandler({InvalidUsernameException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(Exception ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthenticationFailure(NoSuchElementException ex) {
        return ex.getMessage();
    }

    public record AuthRequest(String username, String password) {}
    public record AuthResponse(String token, UserDTO user) {}
}

