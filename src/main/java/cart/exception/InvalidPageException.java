package cart.exception;

import org.springframework.http.HttpStatus;

public class InvalidPageException extends CustomException {

    public InvalidPageException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
