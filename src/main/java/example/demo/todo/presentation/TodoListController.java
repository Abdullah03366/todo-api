package example.demo.todo.presentation;

import example.demo.todo.application.TodoListService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.TodoList;
import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/todolists")
public class TodoListController {

    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping
    public List<TodoList> getAll() {
        return todoListService.findAll();
    }

    @GetMapping("/{id}")
    public TodoList getById(@PathVariable UUID id) {
        return todoListService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoList create(@RequestBody @Valid CreateTodoListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return todoListService.create(request.userId(), request.title(), request.description());
    }

    @PatchMapping("/{id}")
    public TodoList update(@PathVariable UUID id, @RequestBody @Valid UpdateTodoListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return todoListService.update(id, request.title(), request.description());
    }

    @PostMapping("/{todoListId}/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoList addTodo(@PathVariable UUID todoListId, @RequestBody @Valid CreateTodoInListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return todoListService.addTodo(todoListId, request.title(), request.description(), request.priority());
    }

    @DeleteMapping("/{todoListId}/todos/{todoId}")
    public TodoList removeTodo(@PathVariable UUID todoListId, @PathVariable UUID todoId) {
        return todoListService.removeTodo(todoListId, todoId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        todoListService.delete(id);
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

    public record CreateTodoListDTO(UUID userId, String title, String description) {}
    public record CreateTodoInListDTO(String title, String description, Priority priority) {}
    public record UpdateTodoListDTO(String title, String description) {}
}
