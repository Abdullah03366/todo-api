package example.demo.todo.domain.todolist;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;

import java.util.Objects;

public class Description {
    private String description;

    public Description(String description) throws InvalidDescriptionException {
        if (description.length() <= 250 && !description.isBlank()) {
            this.description = Objects.requireNonNull(description, "description must not be null");
        } else {
            throw new InvalidDescriptionException();
        }
    }

    public String getDescription() {
        return description;
    }
}
