package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void constructorInitializesTodoWithDefaults() throws InvalidDescriptionException, InvalidTitleException {
        Todo todo = new Todo("Task", "Description", Priority.HIGH);

        assertNotNull(todo.getId());
        assertEquals("Task", todo.getTitle().getTitle());
        assertEquals("Description", todo.getDescription().getDescription());
        assertFalse(todo.getCompleted());
        assertNotNull(todo.getCreated_at());
        assertEquals("HIGH", todo.getPriority());
    }

    @Test
    void setTitleAndDescriptionAndCompletedAndPriorityUpdatesFields() throws Exception {
        Todo todo = new Todo("Task", "Description", Priority.MEDIUM);

        todo.setTitle(new Title("Updated title"));
        todo.setDescription(new Description("Updated description"));
        todo.setCompleted(true);
        todo.setPriority(Priority.LOW);

        assertEquals("Updated title", todo.getTitle().getTitle());
        assertEquals("Updated description", todo.getDescription().getDescription());
        assertTrue(todo.getCompleted());
        assertEquals("LOW", todo.getPriority());
    }

    @Test
    void constructorRejectsInvalidTitle() {
        assertThrows(InvalidTitleException.class, () -> new Todo(" ", "Description", Priority.HIGH));
    }

    @Test
    void constructorRejectsInvalidDescription() {
        assertThrows(InvalidDescriptionException.class, () -> new Todo("Task", " ", Priority.HIGH));
    }

    @Test
    void toStringContainsKeyFields() throws Exception {
        Todo todo = new Todo("Task", "Description", Priority.HIGH);

        String value = todo.toString();

        assertTrue(value.contains("Todo{"));
        assertTrue(value.contains("title="));
        assertTrue(value.contains("description="));
        assertTrue(value.contains("priority=HIGH"));
    }
}

