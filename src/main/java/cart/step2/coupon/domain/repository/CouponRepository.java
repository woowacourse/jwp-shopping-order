package cart.step2.coupon.domain.repository;

import cart.step2.coupon.domain.Coupon;

import java.util.List;

public interface CouponRepository {

    void updateUsageStatus(final Long memberId, final Long couponId);

    Long create(final Long memberId, final Long couponTypeId);

    List<Coupon> findAll(final Long memberId);

    void deleteById(final Long couponId);

    Coupon findById(final Long couponId);

}
