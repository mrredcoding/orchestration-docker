package efrei.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when invalid credentials are provided during authentication.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidPasswordOrEmailException extends BaseException {

    /**
     * Constructs a new InvalidPasswordOrEmailException with a default error message.
     */
    public InvalidPasswordOrEmailException() {
        super("Incorrect email or password");
    }
}