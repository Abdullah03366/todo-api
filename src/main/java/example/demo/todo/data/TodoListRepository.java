package example.demo.todo.data;

import example.demo.todo.domain.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface TodoListRepository extends JpaRepository<TodoList, UUID> {
    @Override
    @NonNull
    Optional<TodoList> findById(@NonNull UUID id);
}
