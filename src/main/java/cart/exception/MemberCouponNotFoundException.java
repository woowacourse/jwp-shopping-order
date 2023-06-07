package cart.exception;

import org.springframework.http.HttpStatus;

public class MemberCouponNotFoundException extends ApplicationException {

    public MemberCouponNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
