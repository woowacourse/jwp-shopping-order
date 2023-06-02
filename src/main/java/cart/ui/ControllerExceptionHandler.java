package cart.ui;

import cart.dto.ApiErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({CartItemException.NotFound.class, OrderException.NotFound.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.from(e));
    }

    @ExceptionHandler({CartItemException.IllegalMember.class, OrderException.IllegalMember.class})
    public ResponseEntity<ApiErrorResponse> handleAuthorizationException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiErrorResponse.from(e));
    }

}
