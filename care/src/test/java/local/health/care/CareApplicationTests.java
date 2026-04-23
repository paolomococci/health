package local.health.care;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class CareApplicationTests {

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("app.jwt.secret", () -> "____________secret_symmetric_key____________");
	}

	@Autowired
	private WebApplicationContext wac;

	// To verify that the property is actually set.
	@Autowired
	private Environment env;

	@Test
	@DisplayName("Verify that the Spring Boot context was loaded correctly.")
	void contextLoads() throws Exception {
		// 1. Verify that the WebApplicationContext has been injected.
		assertThat(wac).isNotNull();

		// 3. Check that the JWT property has been loaded.
		assertThat(env.getProperty("app.jwt.secret")).isEqualTo("____________secret_symmetric_key____________");
	}
}
