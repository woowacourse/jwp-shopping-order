package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.IllegalPointUsageException;
import cart.exception.InvalidOrderCheckedException;
import cart.exception.InvalidOrderProductException;
import cart.exception.InvalidOrderQuantityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        logger.error("Error from AuthenticationException : ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        logger.error("Error from CartItemException.IllegalMember : ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({
            InvalidOrderCheckedException.class,
            InvalidOrderQuantityException.class,
            InvalidOrderProductException.class,
            IllegalPointUsageException.class
    })
    public ResponseEntity<String> handleCustomApiException(RuntimeException e) {
        logger.error("Error from CustomApiException : ", e);
        return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnexpectedException(Exception exception) {
        logger.error("Error from unexpectedException", exception);
        return ResponseEntity.internalServerError().build();
    }
}
