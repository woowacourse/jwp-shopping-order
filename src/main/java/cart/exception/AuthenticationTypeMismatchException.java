package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationTypeMismatchException extends ApplicationException{
    public AuthenticationTypeMismatchException() {
        super("인증 유형이 일치하지 않습니다.");
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
