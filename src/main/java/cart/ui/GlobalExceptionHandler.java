package cart.ui;

import cart.dto.response.exception.ExceptionResponse;
import cart.dto.response.exception.Payload;
import cart.dto.response.exception.CartItemIdExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import cart.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Payload> handlerAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Payload(response));
    }

    @ExceptionHandler({
            OrderException.IllegalMember.class,
            CartItemException.IllegalMember.class
    })
    public ResponseEntity<Payload> handleException(RuntimeException e) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Payload(response));
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Payload> handleException(OrderException e) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(response));
    }

    @ExceptionHandler(CartItemException.UnknownCartItem.class)
    public ResponseEntity<Payload> handleException(CartItemException.UnknownCartItem e) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = new CartItemIdExceptionResponse(e.getMessage(), e.getUnknownCartItemIds());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(response));
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<Payload> handleException(CartItemException e) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(response));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Payload> handleException(ProductException e) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(response));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Payload> handleException(Exception e) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = new ExceptionResponse("서버에 장애가 발생하였습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Payload(response));
    }

}
