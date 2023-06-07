package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationTypeMismatchException extends ApplicationException {

    public AuthenticationTypeMismatchException(String message) {
        super(message);
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
