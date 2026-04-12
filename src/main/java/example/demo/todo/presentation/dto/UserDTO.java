package example.demo.todo.presentation.dto;

import java.util.List;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        List<TodoListDTO> todoLists
) {}

