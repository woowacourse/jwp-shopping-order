package cart.domain.coupon;

import java.util.ArrayList;
import java.util.List;

public class Coupons {

    private final List<Coupon> coupons;

    public Coupons(final List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public void validateCoupons(final List<Long> couponsId) {
        for (Long couponId : couponsId) {
            coupons.stream()
                    .filter(coupon -> coupon.isSame(couponId))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));
        }
    }

    public List<Coupon> getCouponsByIds(final List<Long> couponsIds) {
        List<Coupon> result = new ArrayList<>();

        for (Long couponId : couponsIds) {
            Coupon coupon = coupons.stream()
                    .filter(it -> it.isSame(couponId))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

            result.add(coupon);
        }

        return result;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
