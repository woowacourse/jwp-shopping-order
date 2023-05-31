package cart.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
