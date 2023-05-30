package coupon.exception;

import common.ui.ApiException;

public class CouponNotFoundException extends ApiException {

    public CouponNotFoundException() {
        super("쿠폰을 찾을 수 없습니다.");
    }
}
