package cart.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleBindException(BindException e) {
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        final String errorMessage = fieldErrors.stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("\n"));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTheOtherExceptions(Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("잠시 후 다시 시도해 주세요");
    }
}
