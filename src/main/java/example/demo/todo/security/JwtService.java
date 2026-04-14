package example.demo.todo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private static final String DEFAULT_SECRET = "demo.todo.local.secret.demo.todo.local.secret";
    private static final long DEFAULT_EXPIRATION_MS = TimeUnit.HOURS.toMillis(1);

    private byte[] secretBytes = DEFAULT_SECRET.getBytes(StandardCharsets.UTF_8);
    private long expirationMs = DEFAULT_EXPIRATION_MS;

    public JwtService() {
    }

    @Autowired
    public void configure(
            @Value("${app.jwt.secret:demo.todo.local.secret.demo.todo.local.secret}") String secret,
            @Value("${app.jwt.expiration-ms:3600000}") long expirationMs
    ) {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMs = expirationMs;
    }

    public JwtService(String secret, long expirationMs) {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMs = expirationMs;
    }

    public String generateToken(AppUserPrincipal principal) {
        long issuedAt = Instant.now().toEpochMilli();
        long expiresAt = issuedAt + expirationMs;
        String payload = principal.getUsername() + "|" + principal.getUserId() + "|" + issuedAt + "|" + expiresAt;
        return encode(payload) + "." + sign(payload);
    }

    public String extractUsername(String token) {
        return parsePayload(token).username();
    }

    public UUID extractUserId(String token) {
        return parsePayload(token).userId();
    }

    public AppUserPrincipal parseToken(String token) {
        TokenPayload payload = parsePayload(token);
        if (payload.expiresAt() < Instant.now().toEpochMilli()) {
            throw new NoSuchElementException("Token expired");
        }
        return new AppUserPrincipal(payload.userId(), payload.username(), "");
    }

    private TokenPayload parsePayload(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            throw new NoSuchElementException("Invalid token");
        }

        String payload = decode(parts[0]);
        if (!sign(payload).equals(parts[1])) {
            throw new NoSuchElementException("Invalid token");
        }

        String[] values = payload.split("\\|");
        if (values.length != 4) {
            throw new NoSuchElementException("Invalid token");
        }

        return new TokenPayload(
                values[0],
                UUID.fromString(values[1]),
                Long.parseLong(values[2]),
                Long.parseLong(values[3])
        );
    }

    private String encode(String value) {
        return URL_ENCODER.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String value) {
        return new String(URL_DECODER.decode(value), StandardCharsets.UTF_8);
    }

    private String sign(String payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secretBytes, HMAC_ALGORITHM));
            return URL_ENCODER.encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Unable to sign token", ex);
        }
    }

    private record TokenPayload(String username, UUID userId, long issuedAt, long expiresAt) {}
}



