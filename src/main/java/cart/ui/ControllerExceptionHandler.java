package cart.ui;

import cart.dto.response.ExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.ItemException;
import cart.exception.CouponException;
import cart.exception.OrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = {
            ItemException.IllegalMember.class,
            OrderException.IllegalMember.class
    })
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = {
            CouponException.class,
            OrderException.class
    })
    public ResponseEntity<ExceptionResponse> handleWrongDiscountInputException(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

}
