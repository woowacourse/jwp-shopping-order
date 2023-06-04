package cart.ui;

import cart.JwpCartApplication;
import cart.dto.ApiErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import cart.exception.PaymentException;
import cart.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(JwpCartApplication.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            final AuthenticationException authenticationException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiErrorResponse.from(authenticationException));
    }

    @ExceptionHandler({CartItemException.NotFound.class, OrderException.NotFound.class,
            ProductException.NotFound.class, PaymentException.NotFound.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(final RuntimeException runtimeException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.from(runtimeException));
    }

    @ExceptionHandler({CartItemException.IllegalMember.class, OrderException.IllegalMember.class})
    public ResponseEntity<ApiErrorResponse> handleAuthorizationException(final RuntimeException runtimeException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiErrorResponse.from(runtimeException));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(final RuntimeException runtimeException) {
        logger.error(runtimeException.getMessage(), runtimeException);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("서버 내부 오류"));
    }

}
