package cart.ui;

import cart.dto.response.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException e) {
        final int statusCode = HttpStatus.UNAUTHORIZED.value();
        final String message = e.getMessage();
        return ResponseEntity.status(statusCode).body(new ErrorResponse(statusCode, message));
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<ErrorResponse> handleException(CartItemException.IllegalMember e) {
        final int statusCode = HttpStatus.FORBIDDEN.value();
        final String message = e.getMessage();
        return ResponseEntity.status(statusCode).body(new ErrorResponse(statusCode, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleCartItemIdsEmpty(final MethodArgumentNotValidException error) {
        final Map<String, String> errorMessage = error.getBindingResult()
                .getAllErrors()
                .stream()
                .map(FieldError.class::cast)
                .collect(Collectors.toUnmodifiableMap(FieldError::getField, ObjectError::getDefaultMessage));

        final int statusCode = HttpStatus.BAD_REQUEST.value();
        final String message = errorMessage.get("cartItemIds");
        return ResponseEntity.status(statusCode).body(new ErrorResponse(statusCode, message));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        final int statusCode = HttpStatus.BAD_REQUEST.value();
        final String message = e.getMessage();
        return ResponseEntity.status(statusCode).body(new ErrorResponse(statusCode, message));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(final NoSuchElementException e) {
        final int statusCode = HttpStatus.BAD_REQUEST.value();
        final String message = e.getMessage();
        return ResponseEntity.status(statusCode).body(new ErrorResponse(statusCode, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        final int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        final String message =  "서버에 일시적인 문제가 생겼습니다. 관리자에게 문의하세요.";
        return ResponseEntity.status(statusCode).body(new ErrorResponse(statusCode, message));
    }

}
