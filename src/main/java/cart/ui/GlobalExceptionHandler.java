package cart.ui;

import cart.dto.response.ExceptionResponse;
import cart.dto.response.Payload;
import cart.dto.response.IdsExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
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
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<ExceptionResponse> handleException(CartItemException.IllegalMember e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(CartItemException.UnknownCartItem.class)
    public ResponseEntity<Payload> handleException(CartItemException.UnknownCartItem e) {
        log.error(e.getMessage(), e);
        IdsExceptionResponse response = new IdsExceptionResponse(e.getMessage(), e.getUnknownCartItemIds());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(response));
    }

    @ExceptionHandler(CartItemException.QuantityNotSame.class)
    public ResponseEntity<Payload> handleException(CartItemException.QuantityNotSame e) {
        log.error(e.getMessage(), e);
        IdsExceptionResponse response = new IdsExceptionResponse(e.getMessage(), e.getStrangeQuantityCartItemIds());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(response));
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<ExceptionResponse> handleException(CartItemException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ExceptionResponse> handleException(ProductException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse("서버에 장애가 발생하였습니다."));
    }

}
