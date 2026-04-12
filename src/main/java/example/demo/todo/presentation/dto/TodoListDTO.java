package example.demo.todo.presentation.dto;

import java.util.List;
import java.util.UUID;

public record TodoListDTO(
        UUID id,
        String title,
        String description,
        List<TodoDTO> todos
) {}

