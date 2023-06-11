package cart.exception;

import org.springframework.http.HttpStatus;

public class InaccessibleCouponException extends ApplicationException {
    public InaccessibleCouponException() {
        super("본인의 쿠폰이 아닙니다");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}
