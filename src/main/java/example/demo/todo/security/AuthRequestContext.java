package example.demo.todo.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class AuthRequestContext {
    public static final String AUTHENTICATED_USER_ATTRIBUTE = "authenticatedUser";

    private AuthRequestContext() {
    }

    public static AppUserPrincipal requireUser(HttpServletRequest request) {
        Object value = request.getAttribute(AUTHENTICATED_USER_ATTRIBUTE);
        if (value instanceof AppUserPrincipal principal) {
            return principal;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid bearer token");
    }
}

