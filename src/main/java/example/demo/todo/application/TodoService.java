package example.demo.todo.application;

import example.demo.todo.data.TodoRepository;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.Todo;
import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo findById(UUID id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
    }

    public Todo update(UUID id, String title, String description, Priority priority, Boolean completed, Date dueAt) throws InvalidTitleException, InvalidDescriptionException {
        Todo todo = findById(id);

        todo.setTitle(new Title(title));
        todo.setDescription(new Description(description));
        if (priority != null) todo.setPriority(priority);
        if (completed != null) todo.setCompleted(completed);
        if (dueAt != null) todo.setDueAt(dueAt);

        return todoRepository.save(todo);
    }

    public void delete(UUID id) {
        if (!todoRepository.existsById(id)) {
            throw new NoSuchElementException("TodoList not found: " + id);
        }
        todoRepository.deleteById(id);
    }
}

