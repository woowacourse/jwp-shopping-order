package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.ShoppingOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ShoppingOrderException.class)
    public ResponseEntity<Void> handleShoppingOrderException(ShoppingOrderException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(FORBIDDEN).build();
    }
}
