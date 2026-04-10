package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.todolist.Title;
import example.demo.todo.domain.todolist.Description;

import java.util.Date;
import java.util.UUID;

public class Todo {
    private UUID id;
    private Title title;
    private Description description;
    private boolean is_completed;
    private Date created_at;
    private Enum<Priority> priority;

    public Todo(String title, String description, Priority priority) throws InvalidTitleException, InvalidDescriptionException {
        this.id = UUID.randomUUID();
        this.title = new Title(title);
        this.description = new Description(description);
        this.is_completed = false;
        this.created_at = new Date();
        this.priority = priority;
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

    public boolean isIs_completed() {
        return is_completed;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Enum<Priority> getPriority() {
        return priority;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    public void setPriority(Enum<Priority> priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", is_completed=" + is_completed +
                ", created_at=" + created_at +
                ", priority=" + priority +
                '}';
    }
}
