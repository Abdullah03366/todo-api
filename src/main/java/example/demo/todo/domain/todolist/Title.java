package example.demo.todo.domain.todolist;

import example.demo.todo.domain.exceptions.InvalidTitleException;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Title {
    private String title;

    public Title() {}

    public Title(String title) throws InvalidTitleException {
        if (title.length() <= 100 && !title.isBlank()) {
            this.title = Objects.requireNonNull(title, "title must not be null");
        } else {
            throw new InvalidTitleException();
        }
    }

    public String getTitle() {
        return title;
    }
}
