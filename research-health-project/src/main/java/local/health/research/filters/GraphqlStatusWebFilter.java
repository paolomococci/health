package local.health.research.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GraphqlStatusWebFilter is a servlet filter that propagates a GraphQL
 * status code stored in the request attributes to the HTTP response.
 * The status value is expected to be set by downstream GraphQL
 * processing code under the key `graphql.httpStatus`.
 */
@Component
public class GraphqlStatusWebFilter implements Filter {

    // Attribute key used to store the desired HTTP status in the request.
    private static final String HTTP_STATUS_ATTR = "graphql.httpStatus";

    /**
     * The filter intercepts every request, allows the rest of the filter chain
     * to execute, and then, if a status code was set during processing,
     * applies that status to the HttpServletResponse.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @param chain    the remaining filter chain
     *
     * @throws IOException      if an I/O error occurs during processing
     * @throws ServletException if a servlet error occurs during processing
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // Pass the request/response pair to the next filter or servlet.
            chain.doFilter(request, response);
        } finally {
            // After the chain has been processed, check whether a status code has been
            // stored in the request attributes.
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                Integer status = (Integer) attrs.getAttribute(HTTP_STATUS_ATTR, RequestAttributes.SCOPE_REQUEST);
                if (status != null && response instanceof HttpServletResponse) {
                    // Apply the status code to the HttpServletResponse.
                    ((HttpServletResponse) response).setStatus(status);
                }
            }
        }
    }

}
