package local.health.research;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The main application class.
@SpringBootApplication
public class ResearchApplication {

	public static void main(String[] args) {
		// The entry point of the Java application.
		// Starts the Spring Boot application context and auto-configures beans.
		SpringApplication.run(ResearchApplication.class, args);
	}

}
