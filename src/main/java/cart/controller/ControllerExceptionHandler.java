package cart.controller;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.BaseException;
import cart.exception.ExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<Void> handleException(AuthenticationException e) {
        log.warn("인증 실패 [이유 : {}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BaseException e) {
        ExceptionType exceptionType = e.getExceptionType();
        log.warn(exceptionType.getErrorMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(exceptionType.getErrorCode());
        return ResponseEntity.status(exceptionType.getHttpStatus()).body(errorResponse);
    }
}
