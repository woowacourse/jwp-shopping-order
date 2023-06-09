package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApplicationException {

    public AuthenticationException() {
        super("사용자 인증에 실패하였습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
