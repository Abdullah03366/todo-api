package example.demo.todo.presentation;

import example.demo.todo.application.TodoListService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.TodoList;
import example.demo.todo.presentation.dto.TodoListDTO;
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
class TodoListControllerTest {

    @Mock
    private TodoListService todoListService;

    @InjectMocks
    private TodoListController todoListController;

    @Test
    void getAllMapsTodoListsToDto() throws Exception {
        when(todoListService.findAll()).thenReturn(List.of(new TodoList("Work", "Desc")));

        List<TodoListDTO> result = todoListController.getAll();

        assertEquals(1, result.size());
        assertEquals("Work", result.getFirst().title());
    }

    @Test
    void getByIdMapsTodoListToDto() throws Exception {
        TodoList todoList = new TodoList("Work", "Desc");
        UUID id = todoList.getId();
        when(todoListService.findById(id)).thenReturn(todoList);

        TodoListDTO result = todoListController.getById(id);

        assertEquals(id, result.id());
        assertEquals("Work", result.title());
    }

    @Test
    void createDelegatesToServiceAndMapsResponse() throws Exception {
        UUID userId = UUID.randomUUID();
        TodoList todoList = new TodoList("Work", "Desc");
        TodoListController.CreateTodoListDTO request = new TodoListController.CreateTodoListDTO(userId, "Work", "Desc");
        when(todoListService.create(userId, "Work", "Desc")).thenReturn(todoList);

        TodoListDTO result = todoListController.create(request);

        assertEquals("Work", result.title());
        verify(todoListService).create(userId, "Work", "Desc");
    }

    @Test
    void updateDelegatesToServiceAndMapsResponse() throws Exception {
        UUID id = UUID.randomUUID();
        TodoList todoList = new TodoList("Updated", "Updated desc");
        TodoListController.UpdateTodoListDTO request = new TodoListController.UpdateTodoListDTO("Updated", "Updated desc");
        when(todoListService.update(id, "Updated", "Updated desc")).thenReturn(todoList);

        TodoListDTO result = todoListController.update(id, request);

        assertEquals("Updated", result.title());
        verify(todoListService).update(id, "Updated", "Updated desc");
    }

    @Test
    void addTodoDelegatesToServiceAndMapsResponse() throws Exception {
        UUID todoListId = UUID.randomUUID();
        TodoList todoList = new TodoList("Work", "Desc");
        todoList.createAndAddTodo("Task", "Task desc", Priority.HIGH);
        TodoListController.CreateTodoInListDTO request =
                new TodoListController.CreateTodoInListDTO("Task", "Task desc", Priority.HIGH);
        when(todoListService.addTodo(todoListId, "Task", "Task desc", Priority.HIGH)).thenReturn(todoList);

        TodoListDTO result = todoListController.addTodo(todoListId, request);

        assertEquals(1, result.todos().size());
        assertEquals("Task", result.todos().getFirst().title());
        verify(todoListService).addTodo(todoListId, "Task", "Task desc", Priority.HIGH);
    }

    @Test
    void deleteDelegatesToService() {
        UUID id = UUID.randomUUID();

        todoListController.delete(id);

        verify(todoListService).delete(id);
    }

    @Test
    void handleNotFoundReturnsMessage() {
        String result = todoListController.handleNotFound(new NoSuchElementException("missing"));

        assertEquals("missing", result);
    }

    @Test
    void handleValidationReturnsMessage() {
        String result = todoListController.handleValidation(new IllegalArgumentException("invalid"));

        assertEquals("invalid", result);
    }
}

