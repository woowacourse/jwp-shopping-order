package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ShoppingCartException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage(), HttpStatus.UNAUTHORIZED);
    }
}
