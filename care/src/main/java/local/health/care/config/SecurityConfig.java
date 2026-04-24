package local.health.care.config;

import local.health.care.filters.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
  private final JwtFilter jwtFilter;

  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  /*
   * Spring Security creates an in-memory user ("user") with a
   * random BCrypt password (printed to the console on startup) if no
   * UserDetailsService is defined elsewhere. This method simply returns
   * that internally constructed AuthenticationManager so it can be
   * injected into other beans, e.g. AuthController.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/h2-console/**",
                "/api/auth/login")
            .permitAll()
            .anyRequest().authenticated())
        // Adds the JWT filter before the standard username/password filter.
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        // Opens headers() so you can call frameOptions().disable()
        .headers(headers -> headers.frameOptions(frame -> frame.disable()));

    return http.build();
  }
}
