package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import example.demo.todo.domain.user.Username;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsAndExceptionsTest {

    @Test
    void titleAcceptsValidValue() throws InvalidTitleException {
        Title title = new Title("Valid title");
        assertEquals("Valid title", title.getTitle());
    }

    @Test
    void titleRejectsBlankOrTooLong() {
        assertThrows(InvalidTitleException.class, () -> new Title(" "));
        assertThrows(InvalidTitleException.class, () -> new Title("a".repeat(101)));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void titleNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Title(null));
    }

    @Test
    void descriptionAcceptsValidValue() throws InvalidDescriptionException {
        Description description = new Description("Valid description");
        assertEquals("Valid description", description.getDescription());
    }

    @Test
    void descriptionRejectsBlankOrTooLong() {
        assertThrows(InvalidDescriptionException.class, () -> new Description(" "));
        assertThrows(InvalidDescriptionException.class, () -> new Description("a".repeat(251)));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void descriptionNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    void usernameAcceptsValidValue() throws InvalidUsernameException {
        Username username = new Username("abdullah");
        assertEquals("abdullah", username.getName());
    }

    @Test
    void usernameRejectsBlankOrTooLong() {
        assertThrows(InvalidUsernameException.class, () -> new Username(" "));
        assertThrows(InvalidUsernameException.class, () -> new Username("a".repeat(51)));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void usernameNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Username(null));
    }

    @Test
    void usernameEqualsAndHashCodeUseNameValue() throws Exception {
        Username a = new Username("same");
        Username b = new Username("same");
        Username c = new Username("different");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(null, a);
        assertNotEquals("text", a);
    }

    @Test
    void exceptionMessagesAreUseful() {
        assertEquals("Title should be shorter than 100 characters", new InvalidTitleException().getMessage());
        assertEquals("Description should be shorter than 250 characters", new InvalidDescriptionException().getMessage());
        assertEquals("Username should be shorter than 50 characters", new InvalidUsernameException().getMessage());
    }

    @Test
    void priorityEnumContainsAllExpectedValues() {
        assertArrayEquals(
                new Priority[]{Priority.HIGH, Priority.MEDIUM, Priority.LOW},
                Priority.values()
        );
    }
}

