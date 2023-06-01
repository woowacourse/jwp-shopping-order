package cart.ui;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(AuthenticationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CartItemException.InvalidQuantity.class, OrderException.InvalidAmount.class})
    public ErrorResponse handleBadRequest(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CartItemException.NoSuchIds.class)
    public ErrorResponse handleNotFound(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
