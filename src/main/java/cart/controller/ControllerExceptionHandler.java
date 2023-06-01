package cart.controller;

import cart.exception.AuthenticationException;
import cart.exception.IllegalMemberException;
import cart.exception.IncorrectPriceException;
import cart.exception.NonExistProductException;
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
    public ResponseEntity<Void> handleException(IllegalMemberException e) {
        log.warn("권한이 없습니다. [이유 : {}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(NonExistProductException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(IncorrectPriceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
