package cart.application.repository;

import cart.domain.coupon.Coupon;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findByMemberId(final Long memberId);
}
