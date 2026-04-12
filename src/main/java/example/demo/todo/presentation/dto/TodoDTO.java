package example.demo.todo.presentation.dto;

import java.util.Date;
import java.util.UUID;

public record TodoDTO(
        UUID id,
        String title,
        String description,
        boolean completed,
        String priority,
        Date dueAt
) {}

