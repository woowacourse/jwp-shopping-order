package cart.controller;

import cart.exception.error.ErrorResponse;
import cart.exception.internal.InternalException;
import cart.exception.network.AuthenticationException;
import cart.exception.network.IllegalCartItemMemberException;
import cart.exception.network.NetworkException;
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

import static java.util.stream.Collectors.joining;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final Exception e) {
        log.error("[ERROR] 예상치 못한 예외가 발생 했습니다. = {}", createErrorMessage(e));
        return ResponseEntity.badRequest().body(new ErrorResponse(9999, "예상치 못한 예외가 발생했습니다."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException e) {
        log.warn("[ERROR] 권한이 없는 사용자입니다. = {}", createErrorMessage(e));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.create(e));
    }

    @ExceptionHandler(IllegalCartItemMemberException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalCartItemMemberException e) {
        log.warn("[ERROR] 권한이 없는 사용자입니다. = {}", createErrorMessage(e));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.create(e));
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorResponse> handleInternalException(final InternalException e) {
        log.error("[ERROR] 서버 에러가 발생했습니다. = {}", createErrorMessage(e));
        return ResponseEntity.internalServerError().body(ErrorResponse.create(e));
    }

    @ExceptionHandler(NetworkException.class)
    public ResponseEntity<ErrorResponse> handleNetworkException(final NetworkException e) {
        log.warn("[ERROR] 네트워크 에러가 발생했습니다.= {}", createErrorMessage(e));
        return ResponseEntity.badRequest().body(ErrorResponse.create(e));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        log.info("[ERROR] 잘못된 값을 입력했습니다. = {}", createErrorMessage(ex));

        final String fieldErrorMessage = ex.getBindingResult().getFieldErrors()
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

