package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.todolist.Title;
import example.demo.todo.domain.todolist.Description;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    private UUID id;

    @Embedded
    @Column(nullable = false, length = 100)
    private Title title;

    @Embedded
    @Column(nullable = false, length = 250)
    private Description description;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(nullable = false)
    private String priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todolist_id", nullable = false)
    private TodoList todoList;

    protected Todo() {
        // nodig voor JPA
    }
    public Todo(TodoList todoList, String title, String description, Priority priority) throws InvalidTitleException, InvalidDescriptionException {
        this.id = UUID.randomUUID();
        this.todoList = todoList;
        this.title = new Title(title);
        this.description = new Description(description);
        this.completed = false;
        this.createdAt = new Date();
        this.priority = priority.toString();
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

    public boolean getCompleted() {
        return completed;
    }

    public Date getCreated_at() {
        return createdAt;
    }

    public String getPriority() {
        return priority;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setPriority(Enum<Priority> priority) {
        this.priority = priority.toString();
    }

    void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", completed=" + completed +
                ", createdAt=" + createdAt +
                ", priority=" + priority +
                '}';
    }
}
