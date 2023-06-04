package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import cart.exception.OrderServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 내부에서 예외가 발생했습니다.";

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handlerAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<String> handleCartItemException(CartItemException.IllegalMember exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> handleOrderException(OrderException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(OrderServerException.class)
    public ResponseEntity<String> handleOrderServerException(OrderServerException exception) {
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR_MESSAGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR_MESSAGE);
    }
}
