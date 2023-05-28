package cart.coupon.application;

import cart.coupon.Coupon;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findAllByMemberId(Long memberId);
}
