package example.demo.todo.application;

import example.demo.todo.data.TodoRepository;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.Todo;
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
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void findAllReturnsRepositoryResult() throws Exception {
        UUID userId = UUID.randomUUID();
        List<Todo> todos = List.of(new Todo("Task", "Description", Priority.HIGH, null));
        when(todoRepository.findAllByTodoList_User_Id(userId)).thenReturn(todos);

        List<Todo> result = todoService.findAll(userId);

        assertEquals(1, result.size());
        verify(todoRepository).findAllByTodoList_User_Id(userId);
    }

    @Test
    void findByIdReturnsTodoWhenPresent() throws Exception {
        UUID userId = UUID.randomUUID();
        Todo todo = new Todo("Task", "Description", Priority.HIGH, null);
        when(todoRepository.findByIdAndTodoList_User_Id(todo.getId(), userId)).thenReturn(Optional.of(todo));

        Todo result = todoService.findById(userId, todo.getId());

        assertSame(todo, result);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        when(todoRepository.findByIdAndTodoList_User_Id(id, userId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> todoService.findById(userId, id));

        assertEquals("Todo not found for user: " + id, ex.getMessage());
    }

    @Test
    void updateChangesAllFieldsWhenProvided() throws Exception {
        UUID userId = UUID.randomUUID();
        Todo todo = new Todo("Old", "Old desc", Priority.MEDIUM, null);
        Date createdAtBefore = todo.getCreated_at();
        Date dueAt = new Date(System.currentTimeMillis() + 86_400_000L);
        UUID id = todo.getId();
        when(todoRepository.findByIdAndTodoList_User_Id(id, userId)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Todo updated = todoService.update(userId, id, "New", "New desc", Priority.HIGH, true, dueAt);

        assertEquals("New", updated.getTitle().getTitle());
        assertEquals("New desc", updated.getDescription().getDescription());
        assertEquals("HIGH", updated.getPriority());
        assertTrue(updated.getCompleted());
        assertEquals(dueAt, updated.getDueAt());
        assertEquals(createdAtBefore, updated.getCreated_at());
        verify(todoRepository).save(todo);
    }

    @Test
    void updateLeavesOptionalFieldsUnchangedWhenNull() throws Exception {
        UUID userId = UUID.randomUUID();
        Date dueAt = new Date(System.currentTimeMillis() + 86_400_000L);
        Todo todo = new Todo("Old", "Old desc", Priority.MEDIUM, dueAt);
        UUID id = todo.getId();
        when(todoRepository.findByIdAndTodoList_User_Id(id, userId)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Todo updated = todoService.update(userId, id, "New", "New desc", null, null, null);

        assertEquals("MEDIUM", updated.getPriority());
        assertFalse(updated.getCompleted());
        assertEquals("New", updated.getTitle().getTitle());
        assertEquals("New desc", updated.getDescription().getDescription());
        assertEquals(dueAt, updated.getDueAt());
    }

    @Test
    void deleteRemovesTodoWhenExisting() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        when(todoRepository.existsByIdAndTodoList_User_Id(id, userId)).thenReturn(true);

        todoService.delete(userId, id);

        verify(todoRepository).deleteById(id);
    }

    @Test
    void deleteThrowsWhenTodoMissing() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        when(todoRepository.existsByIdAndTodoList_User_Id(id, userId)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> todoService.delete(userId, id));

        assertEquals("Todo not found for user: " + id, ex.getMessage());
        verify(todoRepository, never()).deleteById(any(UUID.class));
    }
}

