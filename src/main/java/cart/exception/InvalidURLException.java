package cart.exception;

import org.springframework.http.HttpStatus;

public class InvalidURLException extends ApplicationException {
    public InvalidURLException() {
        super("유효한 URL 형식이 아닙니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
