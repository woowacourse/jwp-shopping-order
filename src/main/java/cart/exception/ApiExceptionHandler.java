package cart.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.StringJoiner;

@ControllerAdvice
public class ApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        StringJoiner messages = new StringJoiner(", ");
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            messages.add(fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(messages.toString());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body("서버 오류가 발생했습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(AuthenticationException e) {
        logger.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(CartItemException.InvalidMember e) {
        logger.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }
}
