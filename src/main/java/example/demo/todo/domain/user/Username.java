package example.demo.todo.domain.user;

import example.demo.todo.domain.exceptions.InvalidUsernameException;

import java.util.Objects;

public class Username {
    private String name;

    public Username(String name) throws InvalidUsernameException {
        if (name.length() <= 50 && !name.isBlank()) {
            this.name = Objects.requireNonNull(name, "name must not be null");
        } else {
            throw new InvalidUsernameException();
        }
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
