package local.health.care.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.UUID;

@Configuration
public class UserDetailsLoggingConfig {

  /**
   * PasswordEncoder to use everywhere in the application.
   * DelegatingPasswordEncoder supports {bcrypt}, {noop}, etc.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    // DelegatingPasswordEncoder (recommended by Spring Security)
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * In-memory user details service that creates the default
   * user "user" with a random raw password, encodes it with the
   * PasswordEncoder above, and logs the encoded value.
   */
  @Bean
  public UserDetailsService userDetailsService(
    PasswordEncoder encoder,
    @Value("${app.test-password:#{null}}") String testPassword
  ) {
    // In-memory manager
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    // 1. Create a raw random password (10-char example)
    String rawPassword = (testPassword != null) 
      ? testPassword
      : UUID.randomUUID().toString().replace("-", "").substring(0, 10);

    // 2. Encode it
    String encodedPassword = encoder.encode(rawPassword);

    // 3. Build the user
    UserDetails user = User.builder()
        .username("user")
        .password(encodedPassword) // already encoded
        .roles("USER")
        .build();

    // 4. Add the user to the manager
    manager.createUser(user);

    // 5. Log the raw and encoded password (so you can copy-paste the raw one)
    System.out.println("--- Generated default user ---");
    System.out.println("Raw password  : " + rawPassword);
    System.out.println("Encoded hash  : " + encodedPassword);
    System.out.println("-------------------------------------");

    return manager;
  }

  /**
   * Keep the AuthenticationManager bean that you already expose
   * for AuthController. No change required.
   */
  @Bean
  public CommandLineRunner runner(UserDetailsService uds) {
    return args -> {
      // The password hash was already printed above.
    };
  }
}