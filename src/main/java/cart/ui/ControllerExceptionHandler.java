package cart.ui;

import cart.dto.response.ExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.ShoppingOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ShoppingOrderException.class)
    public ResponseEntity<ExceptionResponse> handleShoppingOrderException(ShoppingOrderException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.of(exception.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(AuthenticationException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.of("로그인 정보가 맞지 않습니다. 다시 시도해 주세요."));
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<ExceptionResponse> handleException(CartItemException.IllegalMember exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ExceptionResponse.of(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of("예상치 못한 문제가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}
