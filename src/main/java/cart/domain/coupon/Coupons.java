package cart.domain.coupon;

import java.util.List;
import java.util.NoSuchElementException;

public class Coupons {
    private final List<Coupon> coupons;

    public Coupons(final List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public void validateCouponOwnership(List<Long> couponIds) {
        final boolean allCouponsExist = coupons.stream()
                .allMatch(coupon -> couponIds.contains(coupon.getId()));

        if (!allCouponsExist) {
            throw new NoSuchElementException("쿠폰의 소유주가 아닙니다.");
        }
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
