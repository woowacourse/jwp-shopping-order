package cart.exception.cart;

import cart.exception.common.CartNotFoundException;

public class MemberCouponNotFoundException extends CartNotFoundException {
    public MemberCouponNotFoundException() {
        super("쿠폰을 찾을 수 없습니다.");
    }
}
