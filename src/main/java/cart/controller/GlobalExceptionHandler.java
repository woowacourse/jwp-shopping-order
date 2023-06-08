package cart.controller;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.BaseException;
import cart.exception.ExceptionStatus;
import cart.exception.ExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        ExceptionStatus exceptionStatus = ExceptionStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(exceptionStatus.getErrorCode());
        return ResponseEntity.status(exceptionStatus.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(AuthenticationException e) {
        log.warn("인증 실패 [이유 : {}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BaseException e) {
        ExceptionType exceptionType = e.getExceptionType();
        log.warn(exceptionType.getErrorMessage(), e);
        ExceptionStatus exceptionStatus = ExceptionStatus.findByType(exceptionType);
        ErrorResponse errorResponse = new ErrorResponse(exceptionStatus.getErrorCode());
        return ResponseEntity.status(exceptionStatus.getHttpStatus()).body(errorResponse);
    }
}
