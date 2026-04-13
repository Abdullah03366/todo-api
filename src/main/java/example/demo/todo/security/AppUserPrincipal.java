package example.demo.todo.security;

import java.util.UUID;

public class AppUserPrincipal {
    private final UUID userId;
    private final String username;
    private final String passwordHash;

    public AppUserPrincipal(UUID userId, String username, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPassword() {
        return passwordHash;
    }

    public String getUsername() {
        return username;
    }
}


