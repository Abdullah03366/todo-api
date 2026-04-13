package example.demo.todo.data;

import example.demo.todo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Override
    @NonNull
    Optional<User> findById(@NonNull UUID id);
    Optional<User> findByUsername_Name(String name);
    boolean existsByUsername_Name(String name);
}
