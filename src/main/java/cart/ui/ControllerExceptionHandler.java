package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.IllegalMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "cart")
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(IllegalMember.class)
    public ResponseEntity<Void> handleException(IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
