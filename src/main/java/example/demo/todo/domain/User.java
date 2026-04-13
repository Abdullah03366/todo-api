package example.demo.todo.domain;
import example.demo.todo.domain.exceptions.InvalidUsernameException;
import example.demo.todo.domain.user.Username;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "username", nullable = false, length = 50, unique = true))
    private Username username;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoList> todoLists;

    protected User() {
        // nodig voor JPA
    }

    public User(String username, String passwordHash) throws InvalidUsernameException {
        this.id = UUID.randomUUID();
        this.username = new Username(username);
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
        this.todoLists = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public List<TodoList> getTodoLists() {
        return todoLists;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
    }

    public boolean addTodoList(TodoList todoList) {
        todoList.setUser(this);
        return this.todoLists.add(todoList);
    }

    public boolean removeTodoList(TodoList todoList) {
        return this.todoLists.remove(todoList);
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", todoLists=" + todoLists +
                '}';
    }
}
