package cart.controller.exception;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionApiHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<Void> handleException(AuthenticationException e) {
        log.warn("인증 실패 [이유 : {}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BaseException e) {
        ErrorStatus errorStatus = ErrorStatus.from(e.getExceptionType());
        log.warn(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(errorStatus.getErrorCode());
        return ResponseEntity.status(errorStatus.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorStatus errorStatus = ErrorStatus.SERVER_ERROR;
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(errorStatus.getErrorCode());
        return ResponseEntity.status(errorStatus.getHttpStatus()).body(errorResponse);
    }
}
