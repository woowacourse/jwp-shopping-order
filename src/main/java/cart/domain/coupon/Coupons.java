package cart.domain.coupon;

import cart.exception.CouponNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Coupons {

    private final List<Coupon> coupons;

    public Coupons(final List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public void validateHasCoupons(final List<Long> couponsId) {
        for (final Long couponId : couponsId) {
            findCouponById(couponId);
        }
    }

    public List<Coupon> findCouponsByIds(final List<Long> couponsIds) {
        return couponsIds.stream()
                .map(this::findCouponById)
                .collect(Collectors.toList());
    }

    private Coupon findCouponById(Long couponId) {
        return coupons.stream()
                .filter(coupon -> coupon.isSame(couponId))
                .findAny()
                .orElseThrow(CouponNotFoundException::new);
    }


    public List<Coupon> getCoupons() {
        return Collections.unmodifiableList(coupons);
    }
}
