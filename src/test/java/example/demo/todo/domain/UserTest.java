package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.domain.user.Username;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorInitializesFieldsAndEmptyTodoLists() throws Exception {
        User user = new User("abdullah");

        assertNotNull(user.getId());
        assertEquals("abdullah", user.getUsername().getName());
        assertNotNull(user.getTodoLists());
        assertTrue(user.getTodoLists().isEmpty());
    }

    @Test
    void constructorRejectsInvalidUsername() {
        assertThrows(InvalidUsernameException.class, () -> new User(" "));
    }

    @Test
    void addAndRemoveTodoListWorks() throws Exception {
        User user = new User("abdullah");
        TodoList todoList = new TodoList("Work", "Work related todos");

        assertTrue(user.addTodoList(todoList));
        assertEquals(1, user.getTodoLists().size());
        assertTrue(user.removeTodoList(todoList));
        assertTrue(user.getTodoLists().isEmpty());
    }

    @Test
    void removeTodoListReturnsFalseWhenTodoListNotPresent() throws Exception {
        User user = new User("abdullah");
        TodoList todoList = new TodoList("Work", "Work related todos");

        assertFalse(user.removeTodoList(todoList));
    }

    @Test
    void setUsernameUpdatesField() throws Exception {
        User user = new User("abdullah");

        user.setUsername(new Username("asanli"));

        assertEquals("asanli", user.getUsername().getName());
    }

    @Test
    void equalsAndHashCodeBehaveAsExpected() throws Exception {
        User user = new User("abdullah");

        assertSame(user, user);
        assertNotEquals(null, user);
        assertNotEquals("not a user", user);

        User other = new User("abdullah");
        assertNotEquals(user, other);
        assertNotEquals(user.hashCode(), other.hashCode());
    }

    @Test
    void toStringContainsKeyFields() throws Exception {
        User user = new User("abdullah");

        String value = user.toString();

        assertTrue(value.contains("User{"));
        assertTrue(value.contains("username="));
        assertTrue(value.contains("todoLists="));
    }
}

