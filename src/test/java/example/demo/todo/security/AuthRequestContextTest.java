package example.demo.todo.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestContextTest {

    @Test
    void requireUserReturnsPrincipalWhenPresent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        request.setAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE, principal);

        AppUserPrincipal result = AuthRequestContext.requireUser(request);

        assertSame(principal, result);
    }

    @Test
    void requireUserThrowsUnauthorizedWhenMissing() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> AuthRequestContext.requireUser(request));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void requireUserThrowsUnauthorizedWhenWrongAttributeType() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE, "not-a-principal");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> AuthRequestContext.requireUser(request));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }
}

