package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.coupon.discountpolicy.DiscountPolicy;
import cart.domain.coupon.discountpolicy.FixedDiscountPolicy;
import cart.domain.coupon.discountpolicy.NoneDiscountPolicy;
import cart.domain.coupon.discountpolicy.RateDiscountPolicy;
import cart.exception.CouponException;
import cart.exception.ExceptionType;
import java.math.BigDecimal;
import java.util.Arrays;

public enum CouponType {

    RATE(new RateDiscountPolicy(), "RATE"),
    FIXED(new FixedDiscountPolicy(), "FIXED"),
    NONE(new NoneDiscountPolicy(), "NONE"),
    ;

    private final DiscountPolicy discountPolicy;
    private final String couponTypeName;

    CouponType(DiscountPolicy discountPolicy, String couponTypeName) {
        this.discountPolicy = discountPolicy;
        this.couponTypeName = couponTypeName;
    }

    public static CouponType from(String name) {
        return Arrays.stream(CouponType.values())
                .filter(couponType -> couponType.couponTypeName.equals(name))
                .findFirst()
                .orElseThrow(() -> new CouponException(ExceptionType.NOT_FOUND_COUPON));
    }

    public Money discount(Money totalCartsPrice, BigDecimal discountValue) {
        return discountPolicy.discount(totalCartsPrice, discountValue);
    }

    public boolean isValid(BigDecimal discountValue) {
        return discountPolicy.isValid(discountValue);
    }

    public String getCouponTypeName() {
        return couponTypeName;
    }
}
