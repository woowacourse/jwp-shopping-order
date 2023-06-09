package cart.exception.handler;

import cart.exception.ExceptionResponse;
import cart.exception.customexception.CartException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String DEFAULT_MESSAGE = "알 수 없는 오류입니다.";

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ExceptionResponse> handleCartException(CartException e) {
        ExceptionResponse response = new ExceptionResponse(e.getErrorCode().getMessage(), e.getErrorCode().getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParamException(final MethodArgumentNotValidException e) {
        final String errorMessage = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(errorMessage, 101));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ExceptionResponse response = new ExceptionResponse("잘못된 경로입니다.", 102);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        ExceptionResponse response = new ExceptionResponse(DEFAULT_MESSAGE, 100);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
