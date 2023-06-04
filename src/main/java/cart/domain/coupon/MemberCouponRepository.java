package cart.domain.coupon;

public interface MemberCouponRepository {

	MemberCoupon findByMemberId(final Long memberId);

	void deleteByMemberIdAndCouponId(Long memberId, Long couponInfoId);
}
