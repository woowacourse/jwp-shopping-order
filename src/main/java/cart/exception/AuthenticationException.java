package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException {
    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
