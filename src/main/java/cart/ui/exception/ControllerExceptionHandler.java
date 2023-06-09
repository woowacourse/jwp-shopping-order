package cart.ui.exception;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(AuthenticationException e) {
        logger.warn("auth error", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<ExceptionResponse> handleException(CartItemException.IllegalMember e) {
        logger.warn("cartItem error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleException(NoSuchElementException e) {
        logger.warn("no such element error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        logger.error("server error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(e.getMessage());
        return super.handleExceptionInternal(e, body, headers, status, request);
    }
}
