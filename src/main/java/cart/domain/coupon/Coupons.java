package cart.domain.coupon;

import cart.exception.CannotFindCouponException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Coupons {

    private static final Coupons EMTPY_COUPONS = new Coupons(Collections.emptyList());

    private final List<Coupon> coupons;

    public Coupons(final List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public Coupon findCoupon(final Long couponId) {
        return coupons.stream()
                .filter(coupon -> Objects.equals(coupon.getId(), couponId))
                .findAny()
                .orElseThrow(CannotFindCouponException::new);
    }

    public static Coupons empty() {
        return EMTPY_COUPONS;
    }

    public List<Coupon> getCoupons() {
        return List.copyOf(coupons);
    }
}
