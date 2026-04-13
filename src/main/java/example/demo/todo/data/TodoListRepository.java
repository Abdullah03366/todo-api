package example.demo.todo.data;

import example.demo.todo.domain.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoListRepository extends JpaRepository<TodoList, UUID> {
    @Override
    @NonNull
    Optional<TodoList> findById(@NonNull UUID id);

    List<TodoList> findAllByUser_Id(UUID userId);

    Optional<TodoList> findByIdAndUser_Id(UUID todoListId, UUID userId);
}
