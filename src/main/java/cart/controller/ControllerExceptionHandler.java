package cart.controller;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.joining;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleIllegalArgumentException(final Exception e) {
        log.error("[ERROR] 예상치 못한 예외가 발생 했습니다. = {}", createErrorMessage(e));
        return ResponseEntity.badRequest().body("예상치 못한 예외가 발생했습니다.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handlerAuthenticationException(AuthenticationException e) {
        log.warn("[ERROR] 권한이 없는 사용자입니다. = {}", createErrorMessage(e));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 사용자입니다.");
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<String> handleException(CartItemException.IllegalMember e) {
        log.warn("[ERROR] 권한이 없는 사용자입니다. = {}", createErrorMessage(e));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없는 사용자입니다.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStatementException(final IllegalStateException e) {
        log.error("[ERROR] 예외가 발생 했습니다. = {}", createErrorMessage(e));
        return ResponseEntity.internalServerError().body("서버 오류 입니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException e) {
        log.warn("[ERROR] 잘못된 값을 입력했습니다. = {}", createErrorMessage(e));
        return ResponseEntity.badRequest().body("잘못된 값을 입력했습니다.");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(final NoSuchElementException e) {
        log.warn("[ERROR] 해당하는 객체를 찾을 수 없습니다. = {}", createErrorMessage(e));
        return ResponseEntity.badRequest().body("잘못된 값을 입력했습니다.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        log.info("[ERROR] 잘못된 값을 입력했습니다. = {}", createErrorMessage(ex));

        String fieldErrorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining(System.lineSeparator()));

        return ResponseEntity.badRequest().body(fieldErrorMessage);
    }

    private static String createErrorMessage(final Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}

