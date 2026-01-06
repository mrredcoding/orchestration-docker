package efrei.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistsException extends BaseException {

    /**
     * Constructs a new ResourceAlreadyExistsException with a specific error message.
     *
     * @param message the detailed error message.
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}