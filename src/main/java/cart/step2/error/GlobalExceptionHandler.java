package cart.step2.error;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.valueOf;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ShoppingOrderException.class)
    public ResponseEntity<ErrorResponse> handlingApplicationException(final ShoppingOrderException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        log.warn("\nSTATUS : {} \nMESSAGE : {}\n",
                errorCode.getStatus(), errorCode.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(
                        errorCode.getStatus(),
                        errorCode.getMessage()
                ), valueOf(errorCode.getStatus())
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("\nSTATUS : {} \nMESSAGE : {}\n", 400, e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                BAD_REQUEST
        );
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        log.warn("\nSTATUS : {} \nMESSAGE : {}\n", e.getStatusCode(), e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(e.getStatusCode().value(), "잘못된 요청입니다. 올바른 값으로 요청했는지 확인해주세요."),
                BAD_REQUEST
        );
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handlingAnotherException(final RuntimeException exception) {
        log.error("\nSTATUS : {} \nMESSAGE : {}\n", 500, exception.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(500, "알 수 없는 예외가 발생했습니다. 잠시 후 다시 시도해보세요."),
                INTERNAL_SERVER_ERROR
        );
    }
}
