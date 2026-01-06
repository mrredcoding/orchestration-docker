package efrei.backend.configuration.servlets;

import com.mongodb.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Custom implementation of Spring's DispatcherServlet.
 *
 * This servlet adds:
 * - Logging for incoming requests and responses.
 * - Enhanced error handling with detailed logging.
 * - Request timing for performance monitoring.
 */
public class CustomDispatcherServlet extends DispatcherServlet {

    private static final Logger logger = LoggerFactory.getLogger(CustomDispatcherServlet.class);

    /**
     * Overrides the doDispatch method to add custom behavior for logging, error handling, and timing.
     *
     * @param request  The incoming HTTP request.
     * @param response The HTTP response to send back to the client.
     * @throws Exception If an error occurs during request processing.
     */
    @Override
    protected void doDispatch(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws Exception {
        long startTime = System.currentTimeMillis(); // Start timing the request

        try {
            logger.info("Incoming request: Method={}, URI={}, RemoteAddress={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr());

            super.doDispatch(request, response);

        } catch (Exception ex) {
            logger.error("An error occurred while processing the request: URI={}, Error={}",
                    request.getRequestURI(),
                    ex.getMessage(),
                    ex);

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later.");
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("Request processed: URI={}, Duration={} ms",
                    request.getRequestURI(),
                    (endTime - startTime));
        }
    }
}