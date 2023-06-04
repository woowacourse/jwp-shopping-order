package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.CartItemException.CartItemNotExistException;
import cart.exception.InvalidTokenException;
import cart.exception.MemberNotExistException;
import cart.exception.OrderException;
import cart.exception.OrderException.OrderNotExistException;
import cart.exception.ProductException;
import cart.exception.ProductException.ProductNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleIllegalMemberException(CartItemException.IllegalMember e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CartItemException.CartItemNotExistException.class)
    public ResponseEntity<Void> handleCartItemNotExistException(CartItemNotExistException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(MemberNotExistException.class)
    public ResponseEntity<Void> handleMemberNotExistException(MemberNotExistException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Void> handleOrderException(OrderException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(OrderException.OrderNotExistException.class)
    public ResponseEntity<Void> handleOrderNotExistException(OrderNotExistException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Void> handleProductException(ProductException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(ProductException.ProductNotExistException.class)
    public ResponseEntity<Void> handleProductNotExistException(ProductNotExistException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Void> handleProductNotExistException(InvalidTokenException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
