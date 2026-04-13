package example.demo.todo.presentation;

import example.demo.todo.application.TodoListService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.TodoList;
import example.demo.todo.presentation.dto.TodoListDTO;
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
class TodoListControllerTest {

    @Mock
    private TodoListService todoListService;

    @InjectMocks
    private TodoListController todoListController;

    private MockHttpServletRequest request(AppUserPrincipal principal) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE, principal);
        return request;
    }

    @Test
    void getAllMapsTodoListsToDto() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        when(todoListService.findAll(principal.getUserId())).thenReturn(List.of(new TodoList("Work", "Desc")));

        List<TodoListDTO> result = todoListController.getAll(request(principal));

        assertEquals(1, result.size());
        assertEquals("Work", result.getFirst().title());
    }

    @Test
    void getByIdMapsTodoListToDto() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        TodoList todoList = new TodoList("Work", "Desc");
        UUID id = todoList.getId();
        when(todoListService.findById(principal.getUserId(), id)).thenReturn(todoList);

        TodoListDTO result = todoListController.getById(request(principal), id);

        assertEquals(id, result.id());
        assertEquals("Work", result.title());
    }

    @Test
    void createDelegatesToServiceAndMapsResponse() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        TodoList todoList = new TodoList("Work", "Desc");
        TodoListController.CreateTodoListDTO request = new TodoListController.CreateTodoListDTO("Work", "Desc");
        when(todoListService.create(principal.getUserId(), "Work", "Desc")).thenReturn(todoList);

        TodoListDTO result = todoListController.create(request(principal), request);

        assertEquals("Work", result.title());
        verify(todoListService).create(principal.getUserId(), "Work", "Desc");
    }

    @Test
    void updateDelegatesToServiceAndMapsResponse() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        UUID id = UUID.randomUUID();
        TodoList todoList = new TodoList("Updated", "Updated desc");
        TodoListController.UpdateTodoListDTO request = new TodoListController.UpdateTodoListDTO("Updated", "Updated desc");
        when(todoListService.update(principal.getUserId(), id, "Updated", "Updated desc")).thenReturn(todoList);

        TodoListDTO result = todoListController.update(request(principal), id, request);

        assertEquals("Updated", result.title());
        verify(todoListService).update(principal.getUserId(), id, "Updated", "Updated desc");
    }

    @Test
    void addTodoDelegatesToServiceAndMapsResponse() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        UUID todoListId = UUID.randomUUID();
        TodoList todoList = new TodoList("Work", "Desc");
        Date dueAt = new Date(System.currentTimeMillis() + 86_400_000L);
        todoList.createAndAddTodo("Task", "Task desc", Priority.HIGH, dueAt);
        TodoListController.CreateTodoInListDTO request =
                new TodoListController.CreateTodoInListDTO("Task", "Task desc", Priority.HIGH, dueAt);
        when(todoListService.addTodo(principal.getUserId(), todoListId, "Task", "Task desc", Priority.HIGH, dueAt)).thenReturn(todoList);

        TodoListDTO result = todoListController.addTodo(request(principal), todoListId, request);

        assertEquals(1, result.todos().size());
        assertEquals("Task", result.todos().getFirst().title());
        assertEquals(dueAt, result.todos().getFirst().dueAt());
        verify(todoListService).addTodo(principal.getUserId(), todoListId, "Task", "Task desc", Priority.HIGH, dueAt);
    }

    @Test
    void deleteDelegatesToService() {
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        UUID id = UUID.randomUUID();

        todoListController.delete(request(principal), id);

        verify(todoListService).delete(principal.getUserId(), id);
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

