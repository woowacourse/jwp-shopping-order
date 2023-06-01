package cart.exception.coupon;

import cart.exception.common.CartNotFoundException;

public class DiscountPolicyNotFoundException extends CartNotFoundException {
    
    public DiscountPolicyNotFoundException() {
        super("할인 정책을 찾을 수 없습니다.");
    }
}
