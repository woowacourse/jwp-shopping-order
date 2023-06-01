package cart.ui;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException e) {
        log.warn(e.getMessage());
        ErrorResponse response = new ErrorResponse("인증에 실패하였습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthorizationException e) {
        log.warn(e.getMessage());
        ErrorResponse response = new ErrorResponse("권한이 없는 사용자입니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        log.error(e.getCause().getMessage());
        log.error(e.getClass().getName());

        ErrorResponse response = new ErrorResponse("알 수 없는 에러가 발생하였습니다.");
        return ResponseEntity.internalServerError().body(response);
    }
}
