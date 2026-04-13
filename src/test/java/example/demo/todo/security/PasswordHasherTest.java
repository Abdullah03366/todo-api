package example.demo.todo.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void hashAndMatchesReturnTrueForSamePassword() {
        String hash = PasswordHasher.hash("SuperSecret1");

        assertTrue(PasswordHasher.matches("SuperSecret1", hash));
    }

    @Test
    void hashUsesRandomSaltAndProducesDifferentHashes() {
        String hash1 = PasswordHasher.hash("SuperSecret1");
        String hash2 = PasswordHasher.hash("SuperSecret1");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void matchesReturnsFalseForWrongPassword() {
        String hash = PasswordHasher.hash("SuperSecret1");

        assertFalse(PasswordHasher.matches("WrongSecret1", hash));
    }

    @Test
    void matchesReturnsFalseForMalformedThreePartToken() {
        assertFalse(PasswordHasher.matches("SuperSecret1", "not:valid"));
    }

    @Test
    void matchesThrowsWhenIterationPartIsInvalid() {
        assertThrows(NumberFormatException.class,
                () -> PasswordHasher.matches("SuperSecret1", "abc:salt:hash"));
    }
}

