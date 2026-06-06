package local.health.research.resolvers;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import graphql.GraphqlErrorBuilder;
import local.health.research.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityNotFoundExceptionResolver is a Spring component that
 * converts EntityNotFoundException instances, or any exception that
 * wraps one, into GraphQL error objects and sets an HTTP status
 * code for the outgoing response. It extends
 * DataFetcherExceptionResolverAdapter to receive a single-error
 * resolution callback from the Spring GraphQL framework.
 */
@Component
public class EntityNotFoundExceptionResolver extends DataFetcherExceptionResolverAdapter {

    /**
     * Request attribute key used to convey an HTTP status code
     * (404 in this case) back to the GraphQL HTTP filter.
     */
    private static final String HTTP_STATUS_ATTR = "graphql.httpStatus";

    /**
     * SLF4J logger for diagnostic output. DEBUG logs are emitted
     * for the exception type and message; INFO is used when an
     * EntityNotFoundException is actually found.
     */
    private static final Logger logger = LoggerFactory.getLogger(EntityNotFoundExceptionResolver.class);

    /**
     * Resolve a data-fetching exception to a single GraphQLError.
     *
     * @param exception   the original Throwable thrown by a data fetcher
     * @param environment the DataFetchingEnvironment for the current field
     * @return a GraphQLError instance representing the exception,
     *         or null if this resolver does not handle it
     */
    @Override
    public GraphQLError resolveToSingleError(Throwable exception, DataFetchingEnvironment environment) {
        // Log the top-level exception details at DEBUG level.
        logger.debug("Exception type: {}", exception.getClass().getName());
        logger.debug("Exception message: {}", exception.getMessage());

        // Walk through the causal chain to locate an EntityNotFoundException.
        Throwable cause = exception;
        while (cause != null) {
            logger.debug("Checking cause: {}", cause.getClass().getName());

            if (cause instanceof EntityNotFoundException) {
                // Cast to the specific exception type to access its message.
                EntityNotFoundException ex = (EntityNotFoundException) cause;
                logger.info("Found EntityNotFoundException: {}", ex.getMessage());

                /**
                 * If a web request context is available, store the desired
                 * HTTP status code (404) so that downstream filters can
                 * set the actual HTTP response status.
                 */
                RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    attrs.setAttribute(HTTP_STATUS_ATTR, 404, RequestAttributes.SCOPE_REQUEST);
                }

                // Build a GraphQL error object that will be returned to the client.
                return GraphqlErrorBuilder.newError(environment)
                        .message(ex.getMessage())
                        .build();
            }
            // Move to the next cause in the chain.
            cause = cause.getCause();
        }

        // No handled exception found - return null to allow other resolvers.
        return null;
    }
}
