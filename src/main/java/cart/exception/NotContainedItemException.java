package cart.exception;

import org.springframework.http.HttpStatus;

public class NotContainedItemException extends ApplicationException {
    public NotContainedItemException() {
        super("카트에 담기지 않은 상품입니다");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
