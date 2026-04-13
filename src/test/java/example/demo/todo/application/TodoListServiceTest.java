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

import java.util.Date;
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
        UUID userId = UUID.randomUUID();
        List<TodoList> todoLists = List.of(new TodoList("Work", "desc"));
        when(todoListRepository.findAllByUser_Id(userId)).thenReturn(todoLists);

        List<TodoList> result = todoListService.findAll(userId);

        assertEquals(1, result.size());
        verify(todoListRepository).findAllByUser_Id(userId);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        when(todoListRepository.findByIdAndUser_Id(id, userId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> todoListService.findById(userId, id));

        assertEquals("TodoList not found for user: " + id, ex.getMessage());
    }

    @Test
    void createAddsTodoListToUserAndSavesUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User("abdullah", "hash");
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
        UUID userId = UUID.randomUUID();
        TodoList todoList = new TodoList("Old", "Old desc");
        UUID id = todoList.getId();
        when(todoListRepository.findByIdAndUser_Id(id, userId)).thenReturn(Optional.of(todoList));
        when(todoListRepository.save(any(TodoList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updated = todoListService.update(userId, id, "New", "New desc");

        assertEquals("New", updated.getTitle().getTitle());
        assertEquals("New desc", updated.getDescription().getDescription());
        verify(todoListRepository).save(todoList);
    }

    @Test
    void addTodoCreatesTodoInListAndSaves() throws Exception {
        UUID userId = UUID.randomUUID();
        TodoList todoList = new TodoList("Work", "desc");
        Date dueAt = new Date(System.currentTimeMillis() + 86_400_000L);
        UUID id = todoList.getId();
        when(todoListRepository.findByIdAndUser_Id(id, userId)).thenReturn(Optional.of(todoList));
        when(todoListRepository.save(any(TodoList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updated = todoListService.addTodo(userId, id, "Task", "Task desc", Priority.HIGH, dueAt);

        assertEquals(1, updated.getTodos().size());
        assertEquals("Task", updated.getTodos().getFirst().getTitle().getTitle());
        assertEquals(dueAt, updated.getTodos().getFirst().getDueAt());
        verify(todoListRepository).save(todoList);
    }

    @Test
    void removeTodoRemovesLinkedTodoAndSaves() throws Exception {
        UUID userId = UUID.randomUUID();
        TodoList todoList = new TodoList("Work", "desc");
        Todo todo = todoList.createAndAddTodo("Task", "Task desc", Priority.LOW, null);
        UUID listId = todoList.getId();
        UUID todoId = todo.getId();
        when(todoListRepository.findByIdAndUser_Id(listId, userId)).thenReturn(Optional.of(todoList));
        when(todoListRepository.save(any(TodoList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updated = todoListService.removeTodo(userId, listId, todoId);

        assertTrue(updated.getTodos().isEmpty());
        verify(todoListRepository).save(todoList);
    }

    @Test
    void removeTodoThrowsWhenTodoNotLinked() throws Exception {
        UUID userId = UUID.randomUUID();
        TodoList todoList = new TodoList("Work", "desc");
        UUID listId = todoList.getId();
        UUID unknownTodoId = UUID.randomUUID();
        when(todoListRepository.findByIdAndUser_Id(listId, userId)).thenReturn(Optional.of(todoList));

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> todoListService.removeTodo(userId, listId, unknownTodoId));

        assertTrue(ex.getMessage().contains("is not linked to TodoList"));
        verify(todoListRepository, never()).save(any(TodoList.class));
    }

    @Test
    void deleteRemovesWhenExisting() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        when(todoListRepository.findByIdAndUser_Id(id, userId)).thenReturn(Optional.of(mock(TodoList.class)));

        todoListService.delete(userId, id);

        verify(todoListRepository).deleteById(id);
    }

    @Test
    void deleteThrowsWhenMissing() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        when(todoListRepository.findByIdAndUser_Id(id, userId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> todoListService.delete(userId, id));

        assertEquals("TodoList not found for user: " + id, ex.getMessage());
    }
}

