package local.health.care.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Marks this class as a source of bean definitions.
 * Public class that will hold our OpenAPI bean.
 * 
 */
@Configuration
public class OpenApiConfig {

  /**
   * Indicates that the following method produces a bean named `apiInfo` to be
   * registered in the application context.
   */
  @Bean
  public OpenAPI apiInfo() {
    // Create a new OpenAPI instance and attach basic metadata return new
    // OpenAPI().info()
    return new OpenAPI().info(
        // Build the Info object with title, version, and description. This data will be
        // shown by the Swagger UI or any OpenAPI renderer.
        // Title of the API - what users will see, semantic version of the API and short
        // description of what the API does.
        new Info().title("Health Care Integration API").version("0.0.1")
            .description("API for patient/episode/observation"));
  }
}
