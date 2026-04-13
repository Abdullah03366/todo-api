package example.demo.todo.application;

import example.demo.todo.data.UserRepository;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.security.PasswordHasher;
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
        List<User> users = List.of(new User("abdullah", "hash"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void findByIdReturnsUserWhenPresent() throws Exception {
        User user = new User("abdullah", "hash");
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
        User user = new User("abdullah", "hash");
        when(userRepository.findByUsername_Name("abdullah")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("abdullah");

        assertSame(user, result);
        verify(userRepository).findByUsername_Name("abdullah");
    }

    @Test
    void findByUsernameThrowsWhenMissing() {
        when(userRepository.findByUsername_Name("missing")).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> userService.findByUsername("missing"));

        assertEquals("User not found: missing", ex.getMessage());
    }

    @Test
    void createBuildsAndSavesUser() throws InvalidUsernameException {
        when(userRepository.findByUsername_Name("abdullah")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.create("abdullah", "SuperSecret1");

        assertEquals("abdullah", created.getUsername().getName());
        assertTrue(PasswordHasher.matches("SuperSecret1", created.getPasswordHash()));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createRejectsShortPassword() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.create("abdullah", "short"));

        assertEquals("Password must be between 8 and 72 characters", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createRejectsDuplicateUsername() throws InvalidUsernameException {
        User existing = new User("abdullah", PasswordHasher.hash("OtherSecret1"));
        when(userRepository.findByUsername_Name("abdullah")).thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.create("abdullah", "SuperSecret1"));

        assertEquals("Username already exists", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateChangesUsernameAndSaves() throws Exception {
        User user = new User("old", "oldHash");
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername_Name("newname")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.update(id, "newname", "NewSecret1");

        assertEquals("newname", updated.getUsername().getName());
        assertTrue(PasswordHasher.matches("NewSecret1", updated.getPasswordHash()));
        verify(userRepository).save(user);
    }

    @Test
    void updateRejectsDuplicateUsernameForDifferentUser() throws Exception {
        User user = new User("old", "oldHash");
        User other = new User("existing", PasswordHasher.hash("OtherSecret1"));
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername_Name("existing")).thenReturn(Optional.of(other));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.update(id, "existing", "NewSecret1"));

        assertEquals("Username already exists", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateAllowsKeepingOwnUsername() throws Exception {
        User user = new User("old", "oldHash");
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername_Name("old")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.update(id, "old", "NewSecret1");

        assertEquals("old", updated.getUsername().getName());
        assertTrue(PasswordHasher.matches("NewSecret1", updated.getPasswordHash()));
        verify(userRepository).save(user);
    }

    @Test
    void authenticateReturnsUserOnValidPassword() throws Exception {
        String hash = PasswordHasher.hash("SuperSecret1");
        User user = new User("abdullah", hash);
        when(userRepository.findByUsername_Name("abdullah")).thenReturn(Optional.of(user));

        User result = userService.authenticate("abdullah", "SuperSecret1");

        assertSame(user, result);
    }

    @Test
    void authenticateThrowsOnWrongPassword() throws Exception {
        User user = new User("abdullah", PasswordHasher.hash("OtherSecret1"));
        when(userRepository.findByUsername_Name("abdullah")).thenReturn(Optional.of(user));

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> userService.authenticate("abdullah", "SuperSecret1"));

        assertEquals("Invalid credentials", ex.getMessage());
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
