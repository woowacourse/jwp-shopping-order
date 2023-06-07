package cart.exception;

import org.springframework.http.HttpStatus;

public class InvalidPointException extends CustomException {

    public InvalidPointException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
