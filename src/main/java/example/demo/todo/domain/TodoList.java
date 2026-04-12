package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;

import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "todolist_id", nullable = false)
    private List<Todo> todos;

    protected TodoList() {
        // nodig voor JPA
    }

    public TodoList(String title, String description) throws InvalidTitleException, InvalidDescriptionException {
        this.id = UUID.randomUUID();
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

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public boolean addTodo(Todo todo) {
        return this.todos.add(todo);
    }

    public Todo createAndAddTodo(String title, String description, Priority priority, Date dueAt)
            throws InvalidTitleException, InvalidDescriptionException {
        Todo todo = new Todo(title, description, priority, dueAt);
        this.addTodo(todo);
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
