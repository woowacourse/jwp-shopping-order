package cart.domain.coupon;

import cart.exception.GlobalException;

public class CouponName {
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 50;

    private final String name;

    public CouponName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new GlobalException("쿠폰 이름은 1글자 이상, 50글자 이하여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
