package cart.exception.coupon;

import cart.exception.common.CartNotFoundException;

public class CouponNotFoundException extends CartNotFoundException {
    
    public CouponNotFoundException() {
        super("쿠폰을 찾을 수 없습니다.");
    }
}

