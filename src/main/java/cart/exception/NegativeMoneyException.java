package cart.exception;

import org.springframework.http.HttpStatus;

public class NegativeMoneyException extends ApplicationException {

    public NegativeMoneyException() {
        super("금액은 음수가 될 수 없습니다");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
