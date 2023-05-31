package shop.domain.repository;

import shop.domain.coupon.Coupon;
import shop.domain.coupon.CouponType;

import java.util.List;

public interface CouponRepository {
    Long save(Coupon coupon);

    List<Coupon> findAll();

    Coupon findByCouponType(CouponType couponType);

    Coupon findById(Long id);
}
