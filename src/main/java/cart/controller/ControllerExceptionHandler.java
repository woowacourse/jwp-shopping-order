package cart.controller;

import cart.exception.AuthorizationException;
import cart.exception.NotOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Void> handlerAuthorization(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(NotOwnerException.class)
    public ResponseEntity<Void> handleNotOwner(NotOwnerException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
