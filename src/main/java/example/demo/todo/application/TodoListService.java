package example.demo.todo.application;

import example.demo.todo.data.TodoListRepository;
import example.demo.todo.data.UserRepository;
import example.demo.todo.domain.Priority;
import example.demo.todo.domain.Todo;
import example.demo.todo.domain.TodoList;
import example.demo.todo.domain.User;
import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    public TodoListService(TodoListRepository todoListRepository, UserRepository userRepository) {
        this.todoListRepository = todoListRepository;
        this.userRepository = userRepository;
    }

    public List<TodoList> findAll(UUID userId) {
        return todoListRepository.findAllByUser_Id(userId);
    }

    public TodoList findById(UUID userId, UUID id) {
        return todoListRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new NoSuchElementException("TodoList not found for user: " + id));
    }

    public TodoList create(UUID userId, String title, String description)
            throws InvalidTitleException, InvalidDescriptionException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        TodoList todoList = new TodoList(title, description);
        user.addTodoList(todoList);
        userRepository.save(user);
        return todoList;
    }

    public TodoList update(UUID userId, UUID id, String title, String description)
            throws InvalidTitleException, InvalidDescriptionException {
        TodoList todoList = findById(userId, id);
        todoList.setTitle(new Title(title));
        todoList.setDescription(new Description(description));
        return todoListRepository.save(todoList);
    }

    @Transactional
    public TodoList addTodo(UUID userId, UUID todoListId, String title, String description, Priority priority, Date dueAt)
            throws InvalidTitleException, InvalidDescriptionException {
        TodoList todoList = findById(userId, todoListId);
        todoList.createAndAddTodo(title, description, priority, dueAt);

        return todoListRepository.save(todoList);
    }

    @Transactional
    public TodoList removeTodo(UUID userId, UUID todoListId, UUID todoId) {
        TodoList todoList = findById(userId, todoListId);

        Todo linkedTodo = todoList.getTodos().stream()
                .filter(existingTodo -> existingTodo.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Todo " + todoId + " is not linked to TodoList " + todoListId));

        todoList.removeTodo(linkedTodo);
        return todoListRepository.save(todoList);
    }

    public void delete(UUID userId, UUID id) {
        if (todoListRepository.findByIdAndUser_Id(id, userId).isEmpty()) {
            throw new NoSuchElementException("TodoList not found for user: " + id);
        }
        todoListRepository.deleteById(id);
    }
}
