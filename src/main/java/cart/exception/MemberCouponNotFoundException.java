package cart.exception;

import org.springframework.http.HttpStatus;

public class MemberCouponNotFoundException extends ApplicationException {
    public MemberCouponNotFoundException() {
        super("쿠폰을 보유하고 있지 않습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
