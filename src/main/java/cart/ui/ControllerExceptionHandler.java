package cart.ui;

import cart.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({AuthenticationException.class, NoSuchMemberException.class})
    public ResponseEntity<Void> handlerAuthenticationException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({
            NotEnoughStockException.class,
            NotEnoughPointException.class,
            PointOverTotalPriceException.class,
            NoSuchCartItemException.class,
            NoSuchOrderException.class,
            NoSuchProductException.class
    })
    public ResponseEntity<Void> handleOrderException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handlerAnotherException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
