package cart.presentation;

import cart.exception.AuthenticationException;
import cart.exception.IllegalAccessCartException;
import cart.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(final ProductNotFoundException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(final AuthenticationException e) {
        return createResponseEntity(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(IllegalAccessCartException.class)
    public ResponseEntity<String> handleIllegalAccessCartException(final IllegalAccessCartException e) {
        return createResponseEntity(HttpStatus.FORBIDDEN, e.getMessage());
    }

    private ResponseEntity<String> createResponseEntity(final HttpStatus httpStatus, final String message) {
        log.error(message);

        return ResponseEntity.status(httpStatus).body(message);
    }
}
