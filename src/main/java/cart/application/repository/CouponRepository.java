package cart.application.repository;

import cart.domain.Coupon;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findByMemberId(final Long memberId);
}
