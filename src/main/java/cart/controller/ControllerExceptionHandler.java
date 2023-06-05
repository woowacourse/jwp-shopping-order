package cart.controller;

import cart.controller.dto.Payload;
import cart.exception.auth.AuthenticationException;
import cart.exception.cart.CartItemException;
import cart.exception.member.MemberException;
import cart.exception.order.OrderException;
import cart.exception.point.PointException;
import cart.exception.policy.PolicyException;
import cart.exception.product.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Payload> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
        return ResponseEntity.badRequest().body(new Payload(errorMessage));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Payload> handleException(OrderException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(e.getMessage()));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Payload> handleException(ProductException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(e.getMessage()));
    }

    @ExceptionHandler(PointException.class)
    public ResponseEntity<Payload> handleException(PointException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(e.getMessage()));
    }

    @ExceptionHandler(PolicyException.class)
    public ResponseEntity<Payload> handleException(PolicyException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(e.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Payload> handleException(MemberException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Payload> handleException(ValidationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Payload(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Payload> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Payload("서버에 장애가 발생하였습니다"));
    }

}
