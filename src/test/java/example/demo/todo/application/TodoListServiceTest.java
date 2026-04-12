package example.demo.todo.application;

import example.demo.todo.data.TodoListRepository;
import example.demo.todo.data.UserRepository;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.Todo;
import example.demo.todo.domain.TodoList;
import example.demo.todo.domain.User;
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
class TodoListServiceTest {

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoListService todoListService;

    @Test
    void findAllReturnsRepositoryResult() throws Exception {
        List<TodoList> todoLists = List.of(new TodoList("Work", "desc"));
        when(todoListRepository.findAll()).thenReturn(todoLists);

        List<TodoList> result = todoListService.findAll();

        assertEquals(1, result.size());
        verify(todoListRepository).findAll();
    }

    @Test
    void findByIdThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(todoListRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> todoListService.findById(id));

        assertEquals("TodoList not found: " + id, ex.getMessage());
    }

    @Test
    void createAddsTodoListToUserAndSavesUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User("abdullah");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList created = todoListService.create(userId, "Work", "desc");

        assertEquals("Work", created.getTitle().getTitle());
        assertEquals(1, user.getTodoLists().size());
        assertSame(created, user.getTodoLists().getFirst());
        verify(userRepository).save(user);
    }

    @Test
    void createThrowsWhenUserMissing() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> todoListService.create(userId, "Work", "desc"));

        assertEquals("User not found: " + userId, ex.getMessage());
    }

    @Test
    void updateChangesFieldsAndSaves() throws Exception {
        TodoList todoList = new TodoList("Old", "Old desc");
        UUID id = todoList.getId();
        when(todoListRepository.findById(id)).thenReturn(Optional.of(todoList));
        when(todoListRepository.save(any(TodoList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updated = todoListService.update(id, "New", "New desc");

        assertEquals("New", updated.getTitle().getTitle());
        assertEquals("New desc", updated.getDescription().getDescription());
        verify(todoListRepository).save(todoList);
    }

    @Test
    void addTodoCreatesTodoInListAndSaves() throws Exception {
        TodoList todoList = new TodoList("Work", "desc");
        UUID id = todoList.getId();
        when(todoListRepository.findById(id)).thenReturn(Optional.of(todoList));
        when(todoListRepository.save(any(TodoList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updated = todoListService.addTodo(id, "Task", "Task desc", Priority.HIGH);

        assertEquals(1, updated.getTodos().size());
        assertEquals("Task", updated.getTodos().getFirst().getTitle().getTitle());
        verify(todoListRepository).save(todoList);
    }

    @Test
    void removeTodoRemovesLinkedTodoAndSaves() throws Exception {
        TodoList todoList = new TodoList("Work", "desc");
        Todo todo = todoList.createAndAddTodo("Task", "Task desc", Priority.LOW);
        UUID listId = todoList.getId();
        UUID todoId = todo.getId();
        when(todoListRepository.findById(listId)).thenReturn(Optional.of(todoList));
        when(todoListRepository.save(any(TodoList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updated = todoListService.removeTodo(listId, todoId);

        assertTrue(updated.getTodos().isEmpty());
        verify(todoListRepository).save(todoList);
    }

    @Test
    void removeTodoThrowsWhenTodoNotLinked() throws Exception {
        TodoList todoList = new TodoList("Work", "desc");
        UUID listId = todoList.getId();
        UUID unknownTodoId = UUID.randomUUID();
        when(todoListRepository.findById(listId)).thenReturn(Optional.of(todoList));

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> todoListService.removeTodo(listId, unknownTodoId));

        assertTrue(ex.getMessage().contains("is not linked to TodoList"));
        verify(todoListRepository, never()).save(any(TodoList.class));
    }

    @Test
    void deleteRemovesWhenExisting() {
        UUID id = UUID.randomUUID();
        when(todoListRepository.existsById(id)).thenReturn(true);

        todoListService.delete(id);

        verify(todoListRepository).deleteById(id);
    }

    @Test
    void deleteThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(todoListRepository.existsById(id)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> todoListService.delete(id));

        assertEquals("TodoList not found: " + id, ex.getMessage());
    }
}

