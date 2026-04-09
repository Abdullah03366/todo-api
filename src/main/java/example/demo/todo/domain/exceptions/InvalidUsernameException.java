package example.demo.todo.domain.exceptions;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException() {
        super("Username should be shorter than 50 characters");
    }
}
