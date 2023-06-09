package cart.ui.excpetionhandler;

import cart.dto.ExceptionResponse;
import cart.exception.authexception.AuthenticationException;
import cart.exception.authexception.UnauthorizedException;
import cart.exception.illegalexception.IllegalException;
import cart.exception.notfoundexception.NotFoundException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(
        AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalException(IllegalException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        String exceptionMessage = e.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("\n"));
        return ResponseEntity.badRequest().body(new ExceptionResponse(exceptionMessage));
    }

}
