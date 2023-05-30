package coupon.exception;

import common.ui.ApiException;

public class CouponException extends ApiException {

    public CouponException(String message) {
        super(message);
    }

    public CouponException() {
    }
}
