package cart.controller;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.PagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<String> handleCartItemExceptionIllegalMember(CartItemException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(CartItemException.DuplicateProduct.class)
    public ResponseEntity<String> handleCartItemExceptionDuplicateProduct(CartItemException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<String> handleCartItemException(CartItemException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PagingException.class)
    public ResponseEntity<String> handlePagingException(PagingException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final Map<String, String> errorMessageByFields = new HashMap<>();
        final List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        allErrors.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessageByFields.put(fieldName, message);
            logger.warn(message);
        });
        return ResponseEntity.badRequest().body(errorMessageByFields);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception) {
        logger.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("요청 리소스에 맞는 메서드가 아닙니다.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        logger.warn(exception.getMessage());
        return ResponseEntity.badRequest().body("요청 body에서 문제가 발생했습니다. body를 확인해주세요.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 에러가 발생했습니다.");
    }
}
