package local.health.care.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import local.health.care.models.Patient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientApiIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;
    private String token;

    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    @Order(1)
    @DisplayName("A first example of a request that must be forbidden.")
    void testOnStatusGetPatientForbidden() {
        try {
            restClient.get().uri("/api/patients").retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                    }).body(Void.class);
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Order(2)
    @DisplayName("A second example of a request that must be forbidden.")
    void testGetPatientForbidden() {
        try {
            assertThatThrownBy(() -> {
                restClient.get().uri("/api/patients").retrieve().body(Patient.class);
            }).isInstanceOf(HttpClientErrorException.Forbidden.class);
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Order(3)
    @DisplayName("Request that must obtain a valid token.")
    void testGetValidTokenWithHardcodedPassword() {
        // Log in and retrieve a valid token.
        try {
            Map<String, String> loginData = Map.of(
                    "username", "user",
                    "password", "testQwerty123");

            Map<String, Object> authResponse = restClient.post()
                    .uri("/api/auth/login")
                    .body(loginData)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            assertThat(authResponse).isNotNull();
            this.token = authResponse.get("token").toString();

            System.out.println("--- TOKEN RESPONSE ---");
            System.out.println("Full response: " + authResponse);
            // `%n` portable newline depending on the platform.
            System.out.printf("Token: %s%n", this.token);
            System.out.println("----------------------");

            assertThat(this.token).isNotNull().isNotBlank();
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
