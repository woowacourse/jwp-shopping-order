package cart.exception;

import org.springframework.http.HttpStatus;

public class IllegalPercentageException extends ApplicationException {

    public IllegalPercentageException() {
        super("잘못된 백분율입니다");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
