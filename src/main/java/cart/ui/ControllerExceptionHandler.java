package cart.ui;

import cart.dto.response.ExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.ItemException;
import cart.exception.CouponException;
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

    @ExceptionHandler(ItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(ItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(value = {
            CouponException.class
    })
    public ResponseEntity<ExceptionResponse> handleWrongDiscountInputException(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getMessage()));
    }

}
