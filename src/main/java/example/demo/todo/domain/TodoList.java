package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;

import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "todolists")
public class TodoList {
    @Id
    private UUID id;

    @Embedded
    @Column(nullable = false, length = 100)
    private Title title;

    @Embedded
    @Column(nullable = false, length = 250)
    private Description description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "todoList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos;

    protected TodoList() {
        // nodig voor JPA
    }

    public TodoList(User user, String title, String description) throws InvalidTitleException, InvalidDescriptionException {
        this.id = UUID.randomUUID();
        this.user = user;
        this.title = new Title(title);
        this.description = new Description(description);
        this.todos = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    void setUser(User user) {
        this.user = user;
    }

    public boolean addTodo(Todo todo) {
        todo.setTodoList(this);
        return this.todos.add(todo);
    }

    public Todo createAndAddTodo(String title, String description, Priority priority)
            throws InvalidTitleException, InvalidDescriptionException {
        Todo todo = new Todo(this, title, description, priority);
        this.todos.add(todo);
        return todo;
    }

    public boolean removeTodo(Todo todo) {
        return this.todos.remove(todo);
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", todos=" + todos +
                '}';
    }
}
