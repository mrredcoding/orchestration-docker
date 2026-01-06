package efrei.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

    /**
     * Constructs a new ResourceNotFoundException with a specific error message.
     *
     * @param message the detailed error message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}