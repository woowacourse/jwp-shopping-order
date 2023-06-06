package cart.exception.coupon;

import cart.exception.CartException;

public class PolicyTypeNotFoundException extends CartException {
    public PolicyTypeNotFoundException() {
        super("일치하는 할인 정책이 없습니다.");
    }
}
