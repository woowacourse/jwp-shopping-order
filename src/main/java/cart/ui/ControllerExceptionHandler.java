package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(final AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(final CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException e) {
        final Throwable mostSpecificCause = e.getMostSpecificCause();
//        if (mostSpecificCause instanceof NumberFormatException) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mostSpecificCause.getMessage());
    }

}
