package shop.domain.repository;

import shop.domain.coupon.MemberCoupon;

import java.util.List;

public interface MemberCouponRepository {

    Long save(MemberCoupon memberCoupon);

    MemberCoupon findByMemberIdAndCouponId(Long memberId, Long couponId);

    List<MemberCoupon> findAllByMemberId(Long memberId);

    int updateCouponUsingStatus(Long memberCouponId, Boolean isUsed);

}
