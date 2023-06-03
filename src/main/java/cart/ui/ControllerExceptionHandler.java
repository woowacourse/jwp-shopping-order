package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.CartItemException.CartItemNotExistException;
import cart.exception.MemberNotExistException;
import cart.exception.OrderException;
import cart.exception.OrderException.OrderNotExistException;
import cart.exception.ProductException;
import cart.exception.ProductException.ProductNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleIllegalMemberException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CartItemException.CartItemNotExistException.class)
    public ResponseEntity<Void> handleCartItemNotExistException(CartItemNotExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(MemberNotExistException.class)
    public ResponseEntity<Void> handleMemberNotExistException(MemberNotExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Void> handleOrderException(OrderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(OrderException.OrderNotExistException.class)
    public ResponseEntity<Void> handleOrderNotExistException(OrderNotExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Void> handleProductException(ProductException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(ProductException.ProductNotExistException.class)
    public ResponseEntity<Void> handleProductNotExistException(ProductNotExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
