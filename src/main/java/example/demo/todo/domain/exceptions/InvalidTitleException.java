package example.demo.todo.domain.exceptions;

public class InvalidTitleException extends Exception {
    public InvalidTitleException() {
        super("Title should be shorter than 100 characters");
    }
}
