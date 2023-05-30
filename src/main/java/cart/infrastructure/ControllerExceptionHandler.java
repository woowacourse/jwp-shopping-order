package cart.infrastructure;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.MoneyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// TODO 예외메시지 전달, 500 에러 처리
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(final AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<ErrorResponse> handleCartItemException(final CartItemException.IllegalMember exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(CartItemException.IllegalId.class)
    public ResponseEntity<ErrorResponse> handleCartItemException(final CartItemException.IllegalId exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(CartItemException.IllegalQuantity.class)
    public ResponseEntity<ErrorResponse> handleCartItemException(final CartItemException.IllegalQuantity exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MoneyException.IllegalValue.class)
    public ResponseEntity<ErrorResponse> handleMoneyException(final MoneyException.IllegalValue exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(exception.getMessage()));
    }
}
