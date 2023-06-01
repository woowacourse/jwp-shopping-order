package cart.ui.excpetionhandler;

import cart.dto.ExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemNotFoundException;
import cart.exception.CartUnauthorizedException;
import cart.exception.IllegalMoneyException;
import cart.exception.IllegalOrderException;
import cart.exception.IllegalPointException;
import cart.exception.IllegalQuantityException;
import cart.exception.OrderNotFoundException;
import cart.exception.OrderUnauthorizedException;
import cart.exception.ProductNotFoundException;
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

    @ExceptionHandler({
        CartUnauthorizedException.class,
        OrderUnauthorizedException.class})
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({
        IllegalOrderException.class,
        IllegalMoneyException.class,
        IllegalPointException.class,
        IllegalQuantityException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler({
        ProductNotFoundException.class,
        CartItemNotFoundException.class,
        OrderNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ExceptionResponse(e.getMessage()));
    }

}
