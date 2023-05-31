package cart.presentation;

import cart.application.exception.AuthenticationException;
import cart.application.exception.IllegalMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(IllegalMemberException.class)
    public ResponseEntity<Void> handleException(IllegalMemberException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
