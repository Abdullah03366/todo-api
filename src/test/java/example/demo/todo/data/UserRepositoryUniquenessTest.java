package example.demo.todo.data;

import example.demo.todo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserRepositoryUniquenessTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void savingDuplicateUsernameThrowsConstraintViolation() throws Exception {
        userRepository.saveAndFlush(new User("abdullah", "hash1"));

        assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(new User("abdullah", "hash2")));
    }
}

