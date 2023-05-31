package cart.domain.repository;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;

import java.util.List;

public interface CouponRepository {
    Long save(Coupon coupon);

    List<Coupon> findAll();

    Coupon findByCouponType(CouponType couponType);

    Coupon findById(Long id);
}
