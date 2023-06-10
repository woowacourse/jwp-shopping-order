package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApplicationException {

    public AuthenticationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
