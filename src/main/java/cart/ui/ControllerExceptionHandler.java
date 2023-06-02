package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        LOGGER.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        LOGGER.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
