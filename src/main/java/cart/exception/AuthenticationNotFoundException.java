package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationNotFoundException extends ApplicationException{

    public AuthenticationNotFoundException(String message) {
        super(message);
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
