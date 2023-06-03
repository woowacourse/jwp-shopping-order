package cart.application.repository;

import cart.domain.coupon.Coupon;

public interface CouponRepository {

    long create(Coupon coupon);
}
