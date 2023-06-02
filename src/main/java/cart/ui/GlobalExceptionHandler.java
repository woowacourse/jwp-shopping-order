package cart.ui;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger("서버 내부 에러");

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(AuthenticationException e) {
        final ErrorResponse errorResponse = toResponse(UNAUTHORIZED, e);
        return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(CartItemException.IllegalMember e) {
        final ErrorResponse errorResponse = toResponse(FORBIDDEN, e);
        return ResponseEntity.status(FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(final IllegalArgumentException e) {
        final ErrorResponse errorResponse = toResponse(BAD_REQUEST, e);
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(final BindException e) {
        final ErrorResponse errorResponse = toResponse(BAD_REQUEST, e);
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(final Exception e) {
        logger.error("서버 내부 에러 발생: {}", e.getMessage());

        final ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "서버 내부 에러가 발생했습니다.");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private ErrorResponse toResponse(final HttpStatus status, final Exception e) {
        return new ErrorResponse(status.value(), e.getMessage());
    }
}
