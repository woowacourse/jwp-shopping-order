package cart.common;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handle(AuthenticationException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<String> handle(CartItemException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handle(ProductNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
