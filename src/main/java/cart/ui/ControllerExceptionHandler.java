package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.ShoppingException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String ERROR_LOG_FORMAT = "<Exception Class>: %s, <Exception Message>: %s";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(String.format(ERROR_LOG_FORMAT, e.getClass(), e.getMessage()));
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("죄송합니다. 서버에서 오류가 발생했습니다. 잠시 후에 다시 시도해주세요."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String format = "올바르지 않은 타입입니다. 필요한 타입은 [%s]입니다. 현재 입력값: %s";
        String message = String.format(format, e.getRequiredType().getSimpleName(), e.getValue());

        log.warn(String.format(ERROR_LOG_FORMAT, e.getClass(), message));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errorMessages = fieldErrors.stream()
                .map(error -> "[필드명: " + error.getField() + "] " + error.getDefaultMessage())
                .collect(Collectors.toList());
        String message = String.format("요청 데이터이 유효하지 않습니다. 예외 메시지: %s", errorMessages);

        log.warn(String.format(ERROR_LOG_FORMAT, e.getClass(), message));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException e) {
        log.warn(String.format(ERROR_LOG_FORMAT, e.getClass(), e.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ShoppingException.class)
    public ResponseEntity<ErrorResponse> handleShoppingException(ShoppingException e) {
        log.warn(String.format(ERROR_LOG_FORMAT, e.getClass(), e.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }
}
