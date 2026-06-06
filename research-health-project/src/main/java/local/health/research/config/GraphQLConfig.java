package local.health.research.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.schema.GraphQLScalarType;

/**
 * This class configures GraphQL for the application.
 *
 * - @Configuration - tells Spring that this class contains bean definitions.
 * - @Bean - marks a method as a factory for a Spring bean.
 * - RuntimeWiringConfigurer - a functional interface used by Spring GraphQL to
 * customize the runtime wiring (the glue between the schema and its
 * implementations).
 * - GraphQLScalarType - represents a custom scalar (e.g., Date, Long) used
 * in the GraphQL schema.
 */
@Configuration
public class GraphQLConfig {

    /**
     * Creates a RuntimeWiringConfigurer bean that registers a custom scalar.
     *
     * @param localDateScalar the scalar bean that will be injected by Spring
     * @return a lambda implementing RuntimeWiringConfigurer that adds the scalar
     *         to the runtime wiring.
     *
     *         The method uses constructor-style dependency injection: Spring
     *         automatically supplies the required GraphQLScalarType bean when
     *         instantiating this bean.
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType localDateScalar) {
        // Return a lambda that tells the wiring builder to register the scalar.
        return wiringBuilder -> wiringBuilder.scalar(localDateScalar);
    }
}
