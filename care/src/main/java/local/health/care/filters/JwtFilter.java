package local.health.care.filters;

import io.jsonwebtoken.Claims;
import local.health.care.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

/**
 * JwtFilter is a Spring component that intercepts every HTTP request thanks to
 * OncePerRequestFilter and attempts to extract a JWT from the `Authorization`
 * header.
 * If a valid token is found, it populates Spring Security’s
 * SecurityContextHolder so that downstream code can rely on
 * org.springframework.security.core.Authentication being available.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

  // Holds a reference to the utility that knows how to parse and validate JWTs.
  private final JwtUtil jwtUtil;

  /**
   * Constructor injected by Spring; the JwtUtil bean is supplied automatically by
   * the framework.
   * 
   * @param jwtUtil helper that parses tokens into JJWT Claims.
   */
  public JwtFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, jakarta.servlet.ServletException {

    // Grab the `Authorization` header from the incoming HTTP request.
    String header = request.getHeader("Authorization");

    // If the header exists and follows the `Bearer <token>` convention.
    if (header != null && header.startsWith("Bearer ")) {

      // Extract the raw JWT string (skip the "Bearer " prefix).
      String token = header.substring(7);

      try {
        // Parse the token into a Claims object (throws if invalid).
        Claims claims = jwtUtil.parse(token);
        // The subject of the token is conventionally the username/user-id.
        String username = claims.getSubject();
        // If a username was present, build an Authentication object with no credentials
        // and no granted authorities.
        if (username != null) {
          var auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
          // Store the Authentication in the current security context.
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (Exception ex) {
        // invalid token -> clear context and proceed (will be blocked by security)
        SecurityContextHolder.clearContext();
      }
    }
    // Continue with the rest of the filter chain - the request will eventually hit
    // the actual controller (or be blocked by Spring-Security).
    filterChain.doFilter(request, response);
  }
}
