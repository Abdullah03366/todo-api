package example.demo.todo.presentation.dto;

import example.demo.todo.domain.User;
import example.demo.todo.domain.TodoList;
import example.demo.todo.domain.Todo;

public class DTOMapper {

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername().getName(),
                user.getTodoLists().stream()
                        .map(DTOMapper::toTodoListDTO)
                        .toList()
        );
    }

    public static TodoListDTO toTodoListDTO(TodoList todoList) {
        return new TodoListDTO(
                todoList.getId(),
                todoList.getTitle().getTitle(),
                todoList.getDescription().getDescription(),
                todoList.getTodos().stream()
                        .map(DTOMapper::toTodoDTO)
                        .toList()
        );
    }

    public static TodoDTO toTodoDTO(Todo todo) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle().getTitle(),
                todo.getDescription().getDescription(),
                todo.getCompleted(),
                todo.getPriority(),
                todo.getDueAt()
        );
    }
}


