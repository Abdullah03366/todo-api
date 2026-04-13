package example.demo.todo.presentation;

import example.demo.todo.application.TodoService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.Todo;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.presentation.dto.TodoDTO;
import example.demo.todo.security.AuthRequestContext;
import example.demo.todo.security.AppUserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private MockHttpServletRequest request(AppUserPrincipal principal) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE, principal);
        return request;
    }

    @Test
    void getAllMapsTodosToDto() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        Date dueAt = new Date(System.currentTimeMillis() + 86_400_000L);
        when(todoService.findAll(principal.getUserId())).thenReturn(List.of(new Todo("Task", "Desc", Priority.HIGH, dueAt)));

        List<TodoDTO> result = todoController.getAll(request(principal));

        assertEquals(1, result.size());
        assertEquals("Task", result.getFirst().title());
        assertEquals("HIGH", result.getFirst().priority());
        assertEquals(dueAt, result.getFirst().dueAt());
    }

    @Test
    void getByIdMapsTodoToDto() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        Date dueAt = new Date(System.currentTimeMillis() + 86_400_000L);
        Todo todo = new Todo("Task", "Desc", Priority.MEDIUM, dueAt);
        UUID id = todo.getId();
        when(todoService.findById(principal.getUserId(), id)).thenReturn(todo);

        TodoDTO result = todoController.getById(request(principal), id);

        assertEquals(id, result.id());
        assertEquals("Task", result.title());
        assertEquals("MEDIUM", result.priority());
        assertEquals(dueAt, result.dueAt());
    }

    @Test
    void updateDelegatesToServiceAndReturnsDto() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        UUID id = UUID.randomUUID();
        Date dueAt = new Date(System.currentTimeMillis() + 172_800_000L);
        Todo updated = new Todo("New", "New desc", Priority.LOW, dueAt);
        updated.setCompleted(true);
        TodoController.UpdateTodoDTO request = new TodoController.UpdateTodoDTO("New", "New desc", Priority.LOW, true, dueAt);

        when(todoService.update(principal.getUserId(), id, "New", "New desc", Priority.LOW, true, dueAt)).thenReturn(updated);

        TodoDTO result = todoController.update(request(principal), id, request);

        assertEquals("New", result.title());
        assertEquals("LOW", result.priority());
        assertTrue(result.completed());
        assertEquals(dueAt, result.dueAt());
        verify(todoService).update(principal.getUserId(), id, "New", "New desc", Priority.LOW, true, dueAt);
    }

    @Test
    void deleteDelegatesToService() {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        UUID id = UUID.randomUUID();

        todoController.delete(request(principal), id);

        verify(todoService).delete(principal.getUserId(), id);
    }

    @Test
    void handleNotFoundReturnsMessage() {
        NoSuchElementException ex = new NoSuchElementException("missing");

        String result = todoController.handleNotFound(ex);

        assertEquals("missing", result);
    }

    @Test
    void handleValidationReturnsMessage() {
        Exception ex = new InvalidTitleException();

        String result = todoController.handleValidation(ex);

        assertEquals(ex.getMessage(), result);
    }
}


