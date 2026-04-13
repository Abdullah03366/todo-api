package example.demo.todo.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    @Test
    void doFilterWithoutBearerHeaderSkipsAuthAndContinuesChain() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verifyNoInteractions(jwtService);
        verify(filterChain).doFilter(request, response);
        assertNull(request.getAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE));
    }

    @Test
    void doFilterWithValidBearerSetsAuthenticatedUser() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AppUserPrincipal principal = new AppUserPrincipal(UUID.randomUUID(), "abdullah", "hash");
        request.addHeader("Authorization", "Bearer valid-token");
        when(jwtService.parseToken("valid-token")).thenReturn(principal);

        filter.doFilter(request, response, filterChain);

        assertSame(principal, request.getAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE));
        verify(jwtService).parseToken("valid-token");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterWithInvalidBearerSetsNullAuthenticatedUser() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalid-token");
        when(jwtService.parseToken("invalid-token")).thenThrow(new NoSuchElementException("Invalid token"));

        filter.doFilter(request, response, filterChain);

        assertNull(request.getAttribute(AuthRequestContext.AUTHENTICATED_USER_ATTRIBUTE));
        verify(jwtService).parseToken("invalid-token");
        verify(filterChain).doFilter(request, response);
    }
}

