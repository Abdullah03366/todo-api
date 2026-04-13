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

    public List<Todo> findAll(UUID userId) {
        return todoRepository.findAllByTodoList_User_Id(userId);
    }

    public Todo findById(UUID userId, UUID id) {
        return todoRepository.findByIdAndTodoList_User_Id(id, userId)
                .orElseThrow(() -> new NoSuchElementException("Todo not found for user: " + id));
    }

    public Todo update(UUID userId, UUID id, String title, String description, Priority priority, Boolean completed, Date dueAt) throws InvalidTitleException, InvalidDescriptionException {
        Todo todo = findById(userId, id);

        todo.setTitle(new Title(title));
        todo.setDescription(new Description(description));
        if (priority != null) todo.setPriority(priority);
        if (completed != null) todo.setCompleted(completed);
        if (dueAt != null) todo.setDueAt(dueAt);

        return todoRepository.save(todo);
    }

    public void delete(UUID userId, UUID id) {
        if (!todoRepository.existsByIdAndTodoList_User_Id(id, userId)) {
            throw new NoSuchElementException("Todo not found for user: " + id);
        }
        todoRepository.deleteById(id);
    }
}

