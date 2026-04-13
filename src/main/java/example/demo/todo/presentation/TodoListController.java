package example.demo.todo.presentation;

import example.demo.todo.application.TodoListService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.presentation.dto.DTOMapper;
import example.demo.todo.presentation.dto.TodoListDTO;
import example.demo.todo.security.AuthRequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public List<TodoListDTO> getAll(HttpServletRequest request) {
        var principal = AuthRequestContext.requireUser(request);
        return todoListService.findAll(principal.getUserId()).stream()
                .map(DTOMapper::toTodoListDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TodoListDTO getById(HttpServletRequest request, @PathVariable UUID id) {
        var principal = AuthRequestContext.requireUser(request);
        return DTOMapper.toTodoListDTO(todoListService.findById(principal.getUserId(), id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoListDTO create(HttpServletRequest authRequest, @RequestBody @Valid CreateTodoListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        var principal = AuthRequestContext.requireUser(authRequest);
        return DTOMapper.toTodoListDTO(todoListService.create(principal.getUserId(), request.title(), request.description()));
    }

    @PatchMapping("/{id}")
    public TodoListDTO update(HttpServletRequest authRequest, @PathVariable UUID id, @RequestBody @Valid UpdateTodoListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        var principal = AuthRequestContext.requireUser(authRequest);
        return DTOMapper.toTodoListDTO(todoListService.update(principal.getUserId(), id, request.title(), request.description()));
    }

    @PostMapping("/{todoListId}/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoListDTO addTodo(HttpServletRequest authRequest, @PathVariable UUID todoListId, @RequestBody @Valid CreateTodoInListDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        var principal = AuthRequestContext.requireUser(authRequest);
        return DTOMapper.toTodoListDTO(todoListService.addTodo(
                principal.getUserId(),
                todoListId,
                request.title(),
                request.description(),
                request.priority(),
                request.dueAt()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(HttpServletRequest request, @PathVariable UUID id) {
        var principal = AuthRequestContext.requireUser(request);
        todoListService.delete(principal.getUserId(), id);
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

    public record CreateTodoListDTO(String title, String description) {}
    public record CreateTodoInListDTO(String title, String description, Priority priority, Date dueAt) {}
    public record UpdateTodoListDTO(String title, String description) {}
}
