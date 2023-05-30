package cart.domain.repository;

import cart.domain.coupon.Coupon;

import java.util.List;

public interface CouponRepository {
    Long save(Coupon coupon);

    List<Coupon> findAll();

    Coupon findById(Long id);
}
