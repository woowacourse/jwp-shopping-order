package cart.domain.coupon;

import cart.domain.disount.DiscountPolicy;

public class CouponResolver {
    public static Coupon of(Long id, String name, DiscountPolicy discountPolicy, Category category) {
        if (category == Category.ALL) {
            return new ProductCoupon(id, name, discountPolicy);
        }

        if (category == Category.SINGLE) {
            return new SingleCoupon(id, name, discountPolicy);
        }

        throw new IllegalArgumentException("해당 쿠폰을 찾을 수 없습니다.");
    }
}
