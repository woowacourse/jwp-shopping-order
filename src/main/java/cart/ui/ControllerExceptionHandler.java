package cart.ui;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.CartItemNotFoundException;
import cart.exception.NegativePointException;
import cart.exception.NegativePriceException;
import cart.exception.NegativeStockException;
import cart.exception.NotEnoughPointException;
import cart.exception.NotEnoughStockException;
import cart.exception.OrderNotFoundException;
import cart.exception.OrderOwnerException;
import cart.exception.PriceNotMatchException;
import cart.exception.ProductNotFoundException;
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(final AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(final CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleException(final CartItemNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NegativePointException.class)
    public ResponseEntity<String> handleException(final NegativePointException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NegativePriceException.class)
    public ResponseEntity<String> handleException(final NegativePriceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NegativeStockException.class)
    public ResponseEntity<String> handleException(final NegativeStockException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotEnoughPointException.class)
    public ResponseEntity<ErrorResponse> handleException(final NotEnoughPointException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(2, e.getMessage()));
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ErrorResponse> handleException(final NotEnoughStockException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(1, e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleException(final OrderNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(OrderOwnerException.class)
    public ResponseEntity<String> handleException(final OrderOwnerException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PriceNotMatchException.class)
    public ResponseEntity<String> handleException(final PriceNotMatchException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleException(final ProductNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
