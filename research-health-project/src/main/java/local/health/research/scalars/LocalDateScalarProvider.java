package local.health.research.scalars;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import graphql.schema.GraphQLScalarType;

/**
 * LocalDateScalarProvider is a Spring component that exposes a
 * GraphQL scalar type bean to the GraphQL runtime. The scalar
 * is named "Date" and is associated with the LocalDateCoercing
 * implementation defined earlier. By declaring the method
 * as a @Bean, Spring automatically registers the scalar
 * in the application context, making it available for use
 * in the GraphQL schema definitions and resolver wiring.
 */
@Component
public class LocalDateScalarProvider {

  /**
   * The localDateScalar method constructs a GraphQLScalarType
   * instance using the builder API. It sets:
   * - name: "Date" - the scalar name used in the schema.
   * - description: a human-readable explanation of the scalar.
   * - coercing: an instance of LocalDateCoercing which handles
   * parsing, serialization, and literal interpretation.
   * The method returns the built scalar, which is then
   * managed by Spring as a bean.
   */
  @Bean
  public GraphQLScalarType localDateScalar() {
    return GraphQLScalarType.newScalar()
        .name("Date")
        .description("A date in ISO-8601 format (yyyy-MM-dd)")
        .coercing(new LocalDateCoercing())
        .build();
  }
}
