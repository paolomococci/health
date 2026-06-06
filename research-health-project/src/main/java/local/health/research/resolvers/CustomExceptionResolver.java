package local.health.research.resolvers;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import local.health.research.exceptions.EntityNotFoundException;

/**
 * CustomExceptionResolver is a Spring component that
 * translates Java exceptions thrown by GraphQL data fetchers
 * into GraphQL error objects. It extends
 * DataFetcherExceptionResolverAdapter to gain a single-error
 * resolution hook provided by the Spring GraphQL module.
 */
@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {

    /**
     * Attribute name used to communicate an HTTP status code
     * back to the GraphQL HTTP request handling pipeline.
     * The resolver will store 404 here when an entity cannot
     * be found, and the GraphQL filter will read this attribute
     * to set the actual HTTP response status.
     */
    private static final String HTTP_STATUS_ATTR = "graphql.httpStatus";

    /**
     * Override the single-error resolver method. The framework
     * calls this method whenever a data fetcher throws an
     * exception. If the method returns a non-null GraphQLError,
     * the GraphQL response will contain that error.
     *
     * @param exception   the original Java exception
     * @param environment the DataFetchingEnvironment for the current
     *                    field execution
     * @return a GraphQLError object or null if the exception is not
     *         handled by this resolver
     */
    @Override
    public GraphQLError resolveToSingleError(Throwable exception, DataFetchingEnvironment environment) {

        /**
         * Check whether the thrown exception is an EntityNotFoundException.
         * This type of exception indicates that the client requested an
         * entity that does not exist in the database.
         */
        if (exception instanceof EntityNotFoundException) {
            /**
             * Cast the generic Throwable to the specific exception type
             * so that we can access its custom getters.
             */
            EntityNotFoundException ex = (EntityNotFoundException) exception;

            /**
             * Obtain the current request's attributes so we can store
             * the desired HTTP status code for the outgoing response.
             * If the request context is not available (e.g. in a
             * non-web environment) we simply skip setting the status.
             */
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                attrs.setAttribute(HTTP_STATUS_ATTR, 404, RequestAttributes.SCOPE_REQUEST);
            }

            /**
             * Build a GraphQL error object that will be returned to the
             * client. The error message is taken from the exception,
             * and the environment is passed to the builder so that
             * location information can be included automatically.
             */
            return GraphqlErrorBuilder.newError(environment)
                    .message(ex.getMessage())
                    .build();
        }

        /**
         * For any exception that is not an EntityNotFoundException,
         * return null to indicate that this resolver does not handle it.
         * The framework will try the next resolver in the chain.
         */
        return null;
    }
}
