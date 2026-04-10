package example.demo.todo.domain.exceptions;

public class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException() {
        super("Description should be shorter than 250 characters");
    }
}
