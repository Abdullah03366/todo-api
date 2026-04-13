package example.demo.todo.security;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void generateAndParseTokenRoundtrip() {
        JwtService jwtService = new JwtService("test-secret-123456789012345678901234567890", 60_000);
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");

        String token = jwtService.generateToken(principal);
        AppUserPrincipal parsed = jwtService.parseToken(token);

        assertEquals(principal.getUserId(), parsed.getUserId());
        assertEquals(principal.getUsername(), parsed.getUsername());
        assertEquals(principal.getUsername(), jwtService.extractUsername(token));
        assertEquals(principal.getUserId(), jwtService.extractUserId(token));
    }

    @Test
    void parseTokenThrowsForExpiredToken() {
        JwtService jwtService = new JwtService("test-secret-123456789012345678901234567890", -1);
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");

        String token = jwtService.generateToken(principal);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> jwtService.parseToken(token));
        assertEquals("Token expired", ex.getMessage());
    }

    @Test
    void parseTokenThrowsForTamperedSignature() {
        JwtService jwtService = new JwtService("test-secret-123456789012345678901234567890", 60_000);
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");

        String token = jwtService.generateToken(principal);
        String[] parts = token.split("\\.");
        String tamperedToken = parts[0] + "A." + parts[1];

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> jwtService.parseToken(tamperedToken));
        assertEquals("Invalid token", ex.getMessage());
    }

    @Test
    void parseTokenThrowsForInvalidTokenFormat() {
        JwtService jwtService = new JwtService("test-secret-123456789012345678901234567890", 60_000);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> jwtService.parseToken("invalid-token"));
        assertEquals("Invalid token", ex.getMessage());
    }
}

