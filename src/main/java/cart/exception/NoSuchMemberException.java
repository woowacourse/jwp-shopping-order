package cart.exception;

import org.springframework.http.HttpStatus;

public class NoSuchMemberException extends ApplicationException {
    public NoSuchMemberException() {
        super("사용자를 찾지 못했습니다");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
