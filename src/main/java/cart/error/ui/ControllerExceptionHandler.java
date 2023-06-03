package cart.error.ui;

import cart.auth.AuthenticationException;
import cart.cartitem.exception.CartItemException;
import cart.error.ui.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final static String LOG_FORMAT = "Class : {}, Message : {}";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(final AuthenticationException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(final CartItemException.IllegalMember e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        logger.error(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        final String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));

        final ErrorResponse errorResponse = ErrorResponse.from(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
