package example.demo.todo.presentation;

import example.demo.todo.application.TodoService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.Todo;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.presentation.dto.TodoDTO;
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
class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @Test
    void getAllMapsTodosToDto() throws Exception {
        when(todoService.findAll()).thenReturn(List.of(new Todo("Task", "Desc", Priority.HIGH)));

        List<TodoDTO> result = todoController.getAll();

        assertEquals(1, result.size());
        assertEquals("Task", result.getFirst().title());
        assertEquals("HIGH", result.getFirst().priority());
    }

    @Test
    void getByIdMapsTodoToDto() throws Exception {
        Todo todo = new Todo("Task", "Desc", Priority.MEDIUM);
        UUID id = todo.getId();
        when(todoService.findById(id)).thenReturn(todo);

        TodoDTO result = todoController.getById(id);

        assertEquals(id, result.id());
        assertEquals("Task", result.title());
        assertEquals("MEDIUM", result.priority());
    }

    @Test
    void updateDelegatesToServiceAndReturnsDto() throws Exception {
        UUID id = UUID.randomUUID();
        Todo updated = new Todo("New", "New desc", Priority.LOW);
        updated.setCompleted(true);
        TodoController.UpdateTodoDTO request = new TodoController.UpdateTodoDTO("New", "New desc", Priority.LOW, true);

        when(todoService.update(id, "New", "New desc", Priority.LOW, true)).thenReturn(updated);

        TodoDTO result = todoController.update(id, request);

        assertEquals("New", result.title());
        assertEquals("LOW", result.priority());
        assertTrue(result.completed());
        verify(todoService).update(id, "New", "New desc", Priority.LOW, true);
    }

    @Test
    void deleteDelegatesToService() {
        UUID id = UUID.randomUUID();

        todoController.delete(id);

        verify(todoService).delete(id);
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


