package example.demo.todo.domain;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.domain.user.Username;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class User {
    private UUID id;
    private Username username;
    private ArrayList<TodoList> todoLists;

    public User(String username) throws InvalidUsernameException {
        this.id = UUID.randomUUID();
        this.username = new Username(username);
        this.todoLists = new ArrayList<>();
    }

    public boolean addTodoList(TodoList todoList) {
        return this.todoLists.add(todoList);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
