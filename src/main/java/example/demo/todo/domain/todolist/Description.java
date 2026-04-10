package example.demo.todo.domain.todolist;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;

import java.util.Objects;

public class Description {
    private String description;

    public Description(String title) throws InvalidDescriptionException {
        if (title.length() <= 250 && !title.isBlank()) {
            this.description = Objects.requireNonNull(description, "description must not be null");
        } else {
            throw new InvalidDescriptionException();
        }
    }
}
