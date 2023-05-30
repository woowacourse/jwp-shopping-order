package cart.controller;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handlerAuthenticationException(AuthenticationException e) {
        log.warn("[ERROR] 권한이 없는 사용자입니다. = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 사용자입니다.");
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<String> handleException(CartItemException.IllegalMember e) {
        log.warn("[ERROR] 권한이 없는 사용자입니다. = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없는 사용자입니다.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStatementException(final IllegalStateException e) {
        log.error("[ERROR] 예외가 발생 했습니다. = {}", e.getMessage());
        return ResponseEntity.internalServerError().body("서버 오류 입니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException e) {
        log.warn("[ERROR] 예외가 발생 했습니다. = {}", e.getMessage());
        return ResponseEntity.badRequest().body("잘못된 값을 입력했습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleIllegalArgumentException(final Exception e) {
        log.error("[ERROR] 예상치 못한 예외가 발생 했습니다. = {}", e.getMessage());
        return ResponseEntity.badRequest().body("예상치 못한 예외가 발생했습니다.");
    }
}

