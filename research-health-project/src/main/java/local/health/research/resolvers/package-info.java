/**
 * GraphQL resolvers (Query/Mutation/Subscription).
 *
 * Contains classes that expose the GraphQL entry points:
 * - @Controller with @QueryMapping and @MutationMapping
 * - DataFetchers or resolvers for complex types, unions, and interfaces
 *
 * Responsibilities:
 * - Receive GraphQL input (DTO/record) and invoke the service layer
 * - Convert results into GraphQL DTOs if necessary
 * - Not contain direct persistence logic
 */
package local.health.research.resolvers;
