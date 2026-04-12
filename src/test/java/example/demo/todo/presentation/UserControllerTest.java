package example.demo.todo.presentation;

import example.demo.todo.application.UserService;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.presentation.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    @Test
    void getAllMapsUsersToDto() throws Exception {
        when(userService.findAll()).thenReturn(List.of(new User("abdullah")));

        List<UserDTO> result = userController.getAll();

        assertEquals(1, result.size());
        assertEquals("abdullah", result.getFirst().username());
    }

    @Test
    void getByIdMapsUserToDto() throws Exception {
        User user = new User("abdullah");
        UUID id = user.getId();
        when(userService.findById(id)).thenReturn(user);

        UserDTO result = userController.getById(id);

        assertEquals(id, result.id());
        assertEquals("abdullah", result.username());
    }

    @Test
    void createDelegatesToServiceAndMapsResponse() throws Exception {
        User user = new User("abdullah");
        UserController.CreateUserDTO request = new UserController.CreateUserDTO("abdullah");
        when(userService.create("abdullah")).thenReturn(user);

        UserDTO result = userController.create(request);

        assertEquals("abdullah", result.username());
        verify(userService).create("abdullah");
    }

    @Test
    void updateDelegatesToServiceAndMapsResponse() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User("updated");
        UserController.UpdateUserDTO request = new UserController.UpdateUserDTO("updated");
        when(userService.update(id, "updated")).thenReturn(user);

        UserDTO result = userController.update(id, request);

        assertEquals("updated", result.username());
        verify(userService).update(id, "updated");
    }

    @Test
    void deleteDelegatesToService() {
        UUID id = UUID.randomUUID();

        userController.delete(id);

        verify(userService).delete(id);
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

