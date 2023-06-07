package cart.ui;

import cart.exception.*;
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
        LOGGER.warn("An error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleCartItemException(CartItemException.IllegalMember e) {
        LOGGER.warn("An error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(InvalidCardException.class)
    public ResponseEntity<Void> handleInvalidCardException(InvalidCardException e) {
        LOGGER.warn("An error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(InvalidPointException.class)
    public ResponseEntity<Void> handleInvalidPointException(InvalidPointException e) {
        LOGGER.warn("An error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Void> handleInvalidOrderException(OrderException.IllegalMember e) {
        LOGGER.warn("An error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOGGER.error("An error occurred: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body("서버에 오류가 발생했습니다. 관리자에게 문의해주세요");
    }
}
