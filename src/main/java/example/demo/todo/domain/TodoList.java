package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidTitleException;

import example.demo.todo.domain.todolist.Title;

import java.util.UUID;

public class TodoList {
    private UUID id;
    private Title title;
    private String description;

    public TodoList(String title, String description) throws InvalidTitleException {
        this.id = UUID.randomUUID();
        this.title = new Title(title);
        this.description = description;
    }
}
