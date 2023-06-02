package cart.presentation;

import cart.exception.AuthenticationException;
import cart.exception.IllegalAccessCartException;
import cart.exception.IllegalUsePointException;
import cart.exception.InsufficientStockException;
import cart.exception.MemberNotFoundException;
import cart.exception.MismatchedTotalFeeException;
import cart.exception.MismatchedTotalPriceException;
import cart.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(final ProductNotFoundException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(final AuthenticationException e) {
        return createResponseEntity(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(IllegalAccessCartException.class)
    public ResponseEntity<String> handleIllegalAccessCartException(final IllegalAccessCartException e) {
        return createResponseEntity(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(final MemberNotFoundException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStockException(final InsufficientStockException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MismatchedTotalPriceException.class)
    public ResponseEntity<String> handleMismatchedTotalPriceException(final MismatchedTotalPriceException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IllegalUsePointException.class)
    public ResponseEntity<String> handleIllegalUsePointException(final IllegalUsePointException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MismatchedTotalFeeException.class)
    public ResponseEntity<String> handleMismatchedTotalFeeException(final MismatchedTotalFeeException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다. " + e.getMessage());
    }

    private ResponseEntity<String> createResponseEntity(final HttpStatus httpStatus, final String message) {
        log.error(message);

        return ResponseEntity.status(httpStatus).body(message);
    }
}
