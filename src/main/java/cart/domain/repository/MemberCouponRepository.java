package cart.domain.repository;

import cart.domain.coupon.MemberCoupon;

import java.util.List;

public interface MemberCouponRepository {

    Long save(MemberCoupon memberCoupon);

    List<MemberCoupon> findAllByMemberId(Long memberId);

    int updateCouponUsingStatus(Long memberCouponId, Boolean isUsed);

}
