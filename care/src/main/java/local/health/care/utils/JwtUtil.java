package local.health.care.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

  // Holds the raw secret string from configuration.
  private final String secretConfig;

  // Holds the configured token expiration in milliseconds.
  private final long expirationMs;

  // The actual SecretKey instance that will be used for signing/verifying.
  private SecretKey key;

  // Constructor receives two properties:
  // - `${app.jwt.secret}` (required) - the secret key, either raw or
  // Base64-encoded;
  // - `${app.jwt.expiration-ms}` (optional, defaults to 86400000ms = 24h)
  public JwtUtil(@Value("${app.jwt.secret}") String secretConfig,
      @Value("${app.jwt.expiration-ms:86400000}") long expirationMs) {
    this.secretConfig = secretConfig;
    this.expirationMs = expirationMs;
  }

  /**
   * @PostConstruct method is invoked by Spring after the bean is constructed and
   *                all @Value injections are resolved.
   *                It prepares the SecretKey used for JWT signing and
   *                verification.
   */
  @PostConstruct
  private void init() {
    // Validate that the secret has been supplied and is not empty.
    if (secretConfig == null || secretConfig.isBlank()) {
      throw new IllegalStateException("app.jwt.secret not set. Use a Base64 key that is at least 32 bytes long.");
    }

    byte[] keyBytes;
    // Determine if the supplied secret is already Base64-encoded.
    if (isBase64(secretConfig)) {
      // Decode the Base64 string to raw bytes.
      keyBytes = Decoders.BASE64.decode(secretConfig);
    } else {
      // Treat the secret as a plain UTF-8 string.
      keyBytes = secretConfig.getBytes(StandardCharsets.UTF_8);
    }

    // Ensure the key is at least 32 bytes (256 bits) - required by HS256.
    if (keyBytes.length < 32) {
      // Explicit message for debug/dev, recommend Base64.
      throw new IllegalStateException(
          "app.jwt.secret must represent at least 32 bytes (256 bits). Generate a secure key: Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded())");
    }

    // Build a symmetric HMAC key suitable for HS256 signing.
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Create a signed JWT that contains the username as the subject.
   * 
   * @param username
   * @return
   */
  public String generateToken(String username) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(exp)
        // He just takes the key.
        .signWith(key)
        // Serialize the token to a compact string.
        .compact();
  }

  /**
   * Parse a signed JWT and return its Claims payload.
   * Throws JwtException if verification fails.
   * 
   * @param token
   * @return
   * @throws JwtException
   */
  public Claims parse(String token) throws JwtException {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Helper: returns a Base64-safe representation of the key, useful for
   * generating/configuring.
   * 
   * @return
   */
  public static String generateBase64Secret() {
    return Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());
  }

  /**
   * Private helper that checks whether a string looks like a Base64 value.
   * 
   * @param s
   * @return
   */
  private boolean isBase64(String s) {
    return s != null && s.matches("^[A-Za-z0-9+/]+=*$");
  }
}
