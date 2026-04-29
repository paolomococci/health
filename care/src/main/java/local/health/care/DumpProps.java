package local.health.care;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Declares that `DumpProps` is a Spring bean.
 * When Spring scans `local.health.care`,
 * it will instantiate this class (via its constructor) and add it to the
 * application context.
 * 
 * Signals that this bean wants to run some logic immediately after the
 * application starts.
 * Spring Boot automatically detects all beans that implement
 * `CommandLineRunner`
 * and calls their `run` method after the context is ready.
 */
@Component
public class DumpProps implements CommandLineRunner {

  // The field is immutable; it will be assigned once in the constructor and never
  // changed thereafter.
  private final Environment env;

  /**
   * Constructor injection
   * The most recommended way in Spring to supply dependencies.
   * 
   * Spring automatically supplies the current environment instance when creating
   * this bean.
   * Effect: guarantees that the bean is fully configured before any other
   * lifecycle callbacks (e.g., `run`) are invoked.
   * 
   * @param env
   */
  public DumpProps(Environment env) {
    this.env = env;
  }

  /**
   * Confirm that I am overriding the single method from the "CommandLineRunner"
   * interface.
   * `String... args` - Receives the same command-line arguments that were passed
   * to `main`.
   * `@NonNull` - Indicates that `args` should never be `null`. It's a hint for
   * static analysis tools.
   * The ellipsis (`...`) is a *var-args* parameter; it is actually an array
   * internally.
   */
  @Override
  public void run(String @NonNull... args) {
    // Fetching Property Values from `application.yaml` file.
    System.out.println("DATASOURCE URL = " + env.getProperty("spring.datasource.url"));
    System.out.println("SERVER PORT = " + env.getProperty("server.port"));
    System.out.println("APP TEST = " + env.getProperty("app.test"));
  }
}
