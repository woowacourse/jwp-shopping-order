package cart.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationNotFoundException extends ApplicationException{
    public AuthenticationNotFoundException() {
        super("사용자 인증 정보를 찾을 수 없습니다.");
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
