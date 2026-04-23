package local.health.care.controllers;

import local.health.care.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @RestController marks this class as a REST endpoint.
 * @RequestMapping All mappings in this controller are prefixed with
 *                 '/api/auth'.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Dependency that creates JWT strings.
    private final JwtUtil jwtUtil;

    // Dependency that performs authentication against the configured
    // UserDetailsService/authentication provider.
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor‑based injection of the two dependencies.
     * 
     * @param jwtUtil
     * @param authenticationManager
     */
    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * POST '/api/auth/login' - the login endpoint.
     * 
     * @param body
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        // Log the raw values (useful during development, remove in prod).
        System.out.println("Username received: " + body.get("username"));
        System.out.println("Password received: " + body.get("password"));

        // Pull username & password out of the request map.
        String username = body.get("username");
        String password = body.get("password");

        // Quick guard‑clause: both fields must be present.
        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username and password are required!"));
        }

        try {
            // Ask Spring Security to authenticate the supplied credentials.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // If authentication succeeds, generate a JWT for the user.
            String token = jwtUtil.generateToken(username);
            System.out.println("Token generator: " + token);

            // Defensive check - the util should never return null/empty.
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(500)
                        .body(Map.of("error", "Token generation failed!"));
            }

            // Successful login - return the token in a JSON object.
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            // Credentials were wrong - return HTTP 401.
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid credentials."));
        } catch (Exception e) {
            // Unexpected error - log stack trace and return HTTP 500.
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Internal error generating token."));
        }
    }
}