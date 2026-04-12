package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void constructorInitializesTodoWithDefaults() throws InvalidDescriptionException, InvalidTitleException {
        Date dueAt = new Date();
        Todo todo = new Todo("Task", "Description", Priority.HIGH, dueAt);

        assertNotNull(todo.getId());
        assertEquals("Task", todo.getTitle().getTitle());
        assertEquals("Description", todo.getDescription().getDescription());
        assertFalse(todo.getCompleted());
        assertNotNull(todo.getCreated_at());
        assertEquals("HIGH", todo.getPriority());
        assertEquals(dueAt, todo.getDueAt());
    }

    @Test
    void setTitleAndDescriptionAndCompletedAndPriorityUpdatesFields() throws Exception {
        Todo todo = new Todo("Task", "Description", Priority.MEDIUM, null);
        Date updatedDueAt = new Date(System.currentTimeMillis() + 86_400_000L);

        todo.setTitle(new Title("Updated title"));
        todo.setDescription(new Description("Updated description"));
        todo.setCompleted(true);
        todo.setPriority(Priority.LOW);
        todo.setDueAt(updatedDueAt);

        assertEquals("Updated title", todo.getTitle().getTitle());
        assertEquals("Updated description", todo.getDescription().getDescription());
        assertTrue(todo.getCompleted());
        assertEquals("LOW", todo.getPriority());
        assertEquals(updatedDueAt, todo.getDueAt());
    }

    @Test
    void setDueAtDoesNotChangeCreatedAt() throws Exception {
        Todo todo = new Todo("Task", "Description", Priority.MEDIUM, null);
        Date createdAt = todo.getCreated_at();

        todo.setDueAt(new Date(System.currentTimeMillis() + 172_800_000L));

        assertEquals(createdAt, todo.getCreated_at());
    }

    @Test
    void constructorRejectsInvalidTitle() {
        assertThrows(InvalidTitleException.class, () -> new Todo(" ", "Description", Priority.HIGH, null));
    }

    @Test
    void constructorRejectsInvalidDescription() {
        assertThrows(InvalidDescriptionException.class, () -> new Todo("Task", " ", Priority.HIGH, null));
    }

    @Test
    void toStringContainsKeyFields() throws Exception {
        Todo todo = new Todo("Task", "Description", Priority.HIGH, null);

        String value = todo.toString();

        assertTrue(value.contains("Todo{"));
        assertTrue(value.contains("title="));
        assertTrue(value.contains("description="));
        assertTrue(value.contains("priority=HIGH"));
    }
}

