package example.demo.todo.application;

import example.demo.todo.data.UserRepository;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAllReturnsRepositoryResult() throws Exception {
        List<User> users = List.of(new User("abdullah"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void findByIdReturnsUserWhenPresent() throws Exception {
        User user = new User("abdullah");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.findById(user.getId());

        assertSame(user, result);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> userService.findById(id));

        assertEquals("User not found: " + id, ex.getMessage());
    }

    @Test
    void findByUsernameReturnsUserWhenPresent() throws Exception {
        User user = new User("abdullah");
        when(userRepository.findByUsername("abdullah")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("abdullah");

        assertSame(user, result);
        verify(userRepository).findByUsername("abdullah");
    }

    @Test
    void findByUsernameThrowsWhenMissing() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> userService.findByUsername("missing"));

        assertEquals("User not found: missing", ex.getMessage());
    }

    @Test
    void createBuildsAndSavesUser() throws InvalidUsernameException {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.create("abdullah");

        assertEquals("abdullah", created.getUsername().getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateChangesUsernameAndSaves() throws Exception {
        User user = new User("old");
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.update(id, "newname");

        assertEquals("newname", updated.getUsername().getName());
        verify(userRepository).save(user);
    }

    @Test
    void deleteRemovesWhenExisting() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);

        userService.delete(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> userService.delete(id));

        assertEquals("User not found: " + id, ex.getMessage());
        verify(userRepository, never()).deleteById(any(UUID.class));
    }
}
