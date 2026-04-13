package example.demo.todo.presentation;

import example.demo.todo.application.UserService;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.presentation.dto.UserDTO;
import example.demo.todo.security.AuthRequestContext;
import example.demo.todo.security.AppUserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockHttpServletRequest requestWithUser(User user) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE,
                new AppUserPrincipal(user.getId(), user.getUsername().getName(), user.getPasswordHash()));
        return request;
    }

    @Test
    void getCurrentMapsUserToDto() throws Exception {
        User user = new User("abdullah", "hash");
        UUID id = user.getId();
        when(userService.findById(id)).thenReturn(user);

        UserDTO result = userController.getCurrent(requestWithUser(user));

        assertEquals(id, result.id());
        assertEquals("abdullah", result.username());
    }

    @Test
    void updateCurrentDelegatesToServiceAndMapsResponse() throws Exception {
        User user = new User("updated", "newHash");
        UUID id = user.getId();
        UserController.UpdateUserDTO request = new UserController.UpdateUserDTO("updated", "NewSecret1");
        when(userService.update(id, "updated", "NewSecret1")).thenReturn(user);

        UserDTO result = userController.updateCurrent(requestWithUser(user), request);

        assertEquals("updated", result.username());
        verify(userService).update(id, "updated", "NewSecret1");
    }

    @Test
    void deleteCurrentDelegatesToService() throws InvalidUsernameException {
        User user = new User("abdullah", "hash");

        userController.deleteCurrent(requestWithUser(user));

        verify(userService).delete(user.getId());
    }

    @Test
    void handleNotFoundReturnsMessage() {
        String result = userController.handleNotFound(new NoSuchElementException("missing"));

        assertEquals("missing", result);
    }

    @Test
    void handleValidationReturnsMessage() {
        String result = userController.handleValidation(new InvalidUsernameException());

        assertEquals("Username should be shorter than 50 characters", result);
    }
}
