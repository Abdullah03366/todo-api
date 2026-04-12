package example.demo.todo.presentation;

import example.demo.todo.application.TodoListService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.presentation.dto.DTOMapper;
import example.demo.todo.presentation.dto.TodoListDTO;
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
    public List<TodoListDTO> getAll() {
        return todoListService.findAll().stream()
                .map(DTOMapper::toTodoListDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TodoListDTO getById(@PathVariable UUID id) {
        return DTOMapper.toTodoListDTO(todoListService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoListDTO create(@RequestBody @Valid CreateTodoListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return DTOMapper.toTodoListDTO(todoListService.create(request.userId(), request.title(), request.description()));
    }

    @PatchMapping("/{id}")
    public TodoListDTO update(@PathVariable UUID id, @RequestBody @Valid UpdateTodoListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return DTOMapper.toTodoListDTO(todoListService.update(id, request.title(), request.description()));
    }

    @PostMapping("/{todoListId}/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoListDTO addTodo(@PathVariable UUID todoListId, @RequestBody @Valid CreateTodoInListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return DTOMapper.toTodoListDTO(todoListService.addTodo(todoListId, request.title(), request.description(), request.priority()));
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
