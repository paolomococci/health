package local.health.care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CareApplication {

	/**
	 * This is the standard Java entry point.
	 * The JVM looks for a `main` method that is `public`, `static`, returns `void`,
	 * and takes a single `String[]` argument.
	 * The `args` array contains any command-line arguments passed when the
	 * application is started.
	 * 
	 * This static method performs several important tasks:
	 * - creates an `ApplicationContext` (usually a
	 * `ConfigurableApplicationContext`);
	 * - loads bean definitions from the classpath and applies Spring Boot’s
	 * auto-configuration rules;
	 * - performs an early startup (e.g., logging configuration, health checks);
	 * - starts the embedded servlet container if the application is a web
	 * application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(CareApplication.class, args);
	}

}
