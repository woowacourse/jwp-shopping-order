package cart.ui.excpetionhandler;

import cart.dto.ExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.IllegalCartException;
import cart.exception.IllegalMemberException;
import cart.exception.IllegalMoneyException;
import cart.exception.IllegalOrderException;
import cart.exception.IllegalPointException;
import cart.exception.IllegalProductException;
import cart.exception.IllegalQuantityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<ExceptionResponse> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({
        IllegalOrderException.class,
        IllegalCartException.class,
        IllegalMemberException.class,
        IllegalMoneyException.class,
        IllegalPointException.class,
        IllegalProductException.class,
        IllegalQuantityException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }
}
