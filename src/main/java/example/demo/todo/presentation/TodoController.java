package example.demo.todo.presentation;

import example.demo.todo.application.TodoService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.Todo;
import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAll() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable UUID id) {
        return todoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@RequestBody CreateTodoDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return todoService.create(request.title(), request.description(), request.priority());
    }

    @PatchMapping("/{id}")
    public Todo update(@PathVariable UUID id, @RequestBody UpdateTodoDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return todoService.update(id, request.title(), request.description(), request.priority(), request.completed());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public void delete(@PathVariable UUID id) {
        todoService.delete(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoSuchElementException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({InvalidTitleException.class, InvalidDescriptionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(Exception ex) {
        return ex.getMessage();
    }

    public record CreateTodoDTO(String title, String description, Priority priority) {}
    public record UpdateTodoDTO(String title, String description, Priority priority, Boolean completed) {}
}
