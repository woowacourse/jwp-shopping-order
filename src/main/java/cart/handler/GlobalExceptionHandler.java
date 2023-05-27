package cart.handler;

import cart.exception.AuthenticationException;
import cart.exception.CartItemNotFoundException;
import cart.exception.EmailInvalidException;
import cart.exception.MemberNotOwnerException;
import cart.exception.PasswordInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handlerAuthenticationException(final AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(MemberNotOwnerException.class)
    public ResponseEntity<String> handleException(final MemberNotOwnerException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFoundException(final CartItemNotFoundException exception) {
        return responseNotFound(exception);
    }

    @ExceptionHandler(EmailInvalidException.class)
    public ResponseEntity<String> handleEmailInvalidException(final EmailInvalidException exception) {
        return responseBadRequest(exception);
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<String> handlePasswordInvalidException(final PasswordInvalidException exception) {
        return responseBadRequest(exception);
    }

    private ResponseEntity<String> responseNotFound(final Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    private ResponseEntity<String> responseBadRequest(final Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
