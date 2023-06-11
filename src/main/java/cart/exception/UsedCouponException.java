package cart.exception;

import org.springframework.http.HttpStatus;

public class UsedCouponException extends ApplicationException {
    public UsedCouponException() {
        super("이미 사용한 쿠폰은 다시 사용할 수 없다");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
