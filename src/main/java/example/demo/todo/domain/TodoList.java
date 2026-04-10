package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;

import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;

import java.util.ArrayList;
import java.util.UUID;

public class TodoList {
    private UUID id;
    private Title title;
    private Description description;
    private ArrayList<Todo> todos;

    public TodoList(String title, String description) throws InvalidTitleException, InvalidDescriptionException {
        this.id = UUID.randomUUID();
        this.title = new Title(title);
        this.description = new Description(description);
        this.todos = new ArrayList<>();
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
