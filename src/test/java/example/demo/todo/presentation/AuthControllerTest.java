package example.demo.todo.presentation;

import example.demo.todo.application.UserService;
import example.demo.todo.domain.User;
import example.demo.todo.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerCreatesUserAndReturnsToken() throws Exception {
        User user = new User("abdullah", "hash");
        AuthController.AuthRequest request = new AuthController.AuthRequest("abdullah", "SuperSecret1");
        when(userService.create("abdullah", "SuperSecret1")).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("token-1");

        AuthController.AuthResponse response = authController.register(request);

        assertEquals("token-1", response.token());
        assertEquals("abdullah", response.user().username());
        verify(userService).create("abdullah", "SuperSecret1");
    }

    @Test
    void loginAuthenticatesAndReturnsToken() throws Exception {
        User user = new User("abdullah", "hash");
        AuthController.AuthRequest request = new AuthController.AuthRequest("abdullah", "SuperSecret1");
        when(userService.authenticate("abdullah", "SuperSecret1")).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("token-2");

        AuthController.AuthResponse response = authController.login(request);

        assertNotNull(response.user());
        assertEquals("token-2", response.token());
        verify(userService).authenticate("abdullah", "SuperSecret1");
    }

    @Test
    void handleAuthenticationFailureReturnsMessage() {
        String message = authController.handleAuthenticationFailure(new NoSuchElementException("Invalid credentials"));

        assertEquals("Invalid credentials", message);
    }
}

