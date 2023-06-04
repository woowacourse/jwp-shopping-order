package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.coupon.discountpolicy.DiscountPolicy;
import cart.domain.coupon.discountpolicy.FixedDiscountPolicy;
import cart.domain.coupon.discountpolicy.NoneDiscountPolicy;
import cart.domain.coupon.discountpolicy.RateDiscountPolicy;
import java.math.BigDecimal;

public enum CouponType {

    RATE(new RateDiscountPolicy()),
    FIXED(new FixedDiscountPolicy()),
    NONE(new NoneDiscountPolicy()),
    ;

    private final DiscountPolicy discountPolicy;

    CouponType(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public Money discount(Money totalCartsPrice, BigDecimal discountValue) {
        return discountPolicy.discount(totalCartsPrice, discountValue);
    }

    public boolean isValid(BigDecimal discountValue) {
        return discountPolicy.isValid(discountValue);
    }
}
