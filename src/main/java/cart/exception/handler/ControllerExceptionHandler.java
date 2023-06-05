package cart.exception.handler;

import cart.exception.AuthenticationException;
import cart.exception.ForbiddenException;
import cart.exception.InvalidRequestException;
import cart.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> authenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Void> forbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> notFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            InvalidRequestException.class,
            MethodArgumentNotValidException.class }
    )
    public ResponseEntity<Void> badRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
