package cart.ui;

import cart.dto.response.ShoppingOrderResponse;
import cart.dto.response.ShoppingOrderResultResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemNotFoundException;
import cart.exception.NumberRangeException;
import cart.exception.ShoppingOrderException;
import cart.exception.UnauthorizedAccessException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String BAD_REQUEST_MESSAGE = "잘못된 요청입니다.";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        Map<String, String> validation = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            validation.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(new ShoppingOrderResultResponse<>(BAD_REQUEST_MESSAGE, validation));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ShoppingOrderResponse> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(new ShoppingOrderResponse("서버에 알 수 없는 문제가 발생했습니다."));
    }

    @ExceptionHandler(ShoppingOrderException.class)
    public ResponseEntity<ShoppingOrderResponse> handleShoppingOrderException(ShoppingOrderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ShoppingOrderResponse(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ShoppingOrderResponse> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ShoppingOrderResponse("인증이 실패했습니다."));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ShoppingOrderResponse> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ShoppingOrderResponse(e.getMessage()));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ShoppingOrderResponse> handleCartItemNotFoundException(CartItemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ShoppingOrderResponse(e.getMessage()));
    }

    @ExceptionHandler(NumberRangeException.class)
    public ResponseEntity<ShoppingOrderResponse> handleNumberRangeException(NumberRangeException e) {
        Map<String, String> validation = new HashMap<>();
        validation.put(e.getField(), e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ShoppingOrderResultResponse<>(BAD_REQUEST_MESSAGE, validation));
    }
}
