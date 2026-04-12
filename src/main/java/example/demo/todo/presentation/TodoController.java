package example.demo.todo.presentation;

import example.demo.todo.application.TodoService;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.presentation.dto.DTOMapper;
import example.demo.todo.presentation.dto.TodoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public List<TodoDTO> getAll() {
        return todoService.findAll().stream()
                .map(DTOMapper::toTodoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TodoDTO getById(@PathVariable UUID id) {
        return DTOMapper.toTodoDTO(todoService.findById(id));
    }

    @PatchMapping("/{id}")
    public TodoDTO update(@PathVariable UUID id, @RequestBody UpdateTodoDTO request)
            throws InvalidTitleException, InvalidDescriptionException {
        return DTOMapper.toTodoDTO(todoService.update(
                id,
                request.title(),
                request.description(),
                request.priority(),
                request.completed(),
                request.dueAt()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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

    public record UpdateTodoDTO(String title, String description, Priority priority, Boolean completed, Date dueAt) {}
}
