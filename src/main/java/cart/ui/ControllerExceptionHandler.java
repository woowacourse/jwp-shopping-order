package cart.ui;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.NotEnoughPointException;
import cart.exception.NotEnoughStockException;
import cart.exception.ShoppingOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<String> handleException(final Exception e) {
        return ResponseEntity.internalServerError().body("예상치 못한 예외가 발생했습니다.");
    }

    @ExceptionHandler(ShoppingOrderException.class)
    private ResponseEntity<String> handleException(final ShoppingOrderException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(final AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(final CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(NotEnoughPointException.class)
    public ResponseEntity<ErrorResponse> handleException(final NotEnoughPointException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(2, e.getMessage()));
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ErrorResponse> handleException(final NotEnoughStockException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(1, e.getMessage()));
    }
}
