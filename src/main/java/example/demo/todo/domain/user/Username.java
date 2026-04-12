package example.demo.todo.domain.user;

import example.demo.todo.domain.exceptions.InvalidUsernameException;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Username {
    private String name;

    protected Username() {
        // nodig voor JPA
    }

    public Username(String name) throws InvalidUsernameException {
        if (name.length() <= 50 && !name.isBlank()) {
            this.name = Objects.requireNonNull(name, "name must not be null");
        } else {
            throw new InvalidUsernameException();
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Username username)) return false;
        return Objects.equals(name, username.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
