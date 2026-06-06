package local.health.research;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The ResearchApplication class is the primary entry point of the
 * Spring Boot application. It is located in the base package
 * local.health.research so that component scanning picks up all
 * beans defined in subpackages.
 *
 * SpringBootApplication is a convenience annotation that
 * combines @Configuration, @EnableAutoConfiguration and
 * @ComponentScan. It tells Spring Boot to start auto-configuration
 * and to look for components in the current package and below.
 */
@SpringBootApplication
public class ResearchApplication {

	/**
	 * Application entry point for a Spring Boot application.
	 * 
	 * The main method is the standard Java entry point and delegates to
	 * SpringApplication.run, which performs the following steps:
	 * - Creates and starts a ConfigurableApplicationContext.
	 * - Scans for components, via @ComponentScan, and registers all discovered
	 * beans.
	 * - Applies auto-configuration rules, @EnableAutoConfiguration.
	 * - Detects the runtime environment; if a web environment is present, it starts
	 * an embedded servlet container.
	 * - Passes command-line arguments to any CommandLineRunner or ApplicationRunner
	 * beans.
	 * - Returns the created ApplicationContext for possible further programmatic
	 * use.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		// Starts the Spring Boot application context and auto-configures beans.
		SpringApplication.run(ResearchApplication.class, args);
	}

}
