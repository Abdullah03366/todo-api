package example.demo.todo.data;

import example.demo.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    @Override
    @NonNull
    Optional<Todo> findById(@NonNull UUID id);

    List<Todo> findAllByTodoList_User_Id(UUID userId);

    Optional<Todo> findByIdAndTodoList_User_Id(UUID todoId, UUID userId);

    boolean existsByIdAndTodoList_User_Id(UUID todoId, UUID userId);
}
