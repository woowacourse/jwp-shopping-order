package cart.domain.coupon;

public interface MemberCouponRepository {

	Long addCoupon(final Long memberId, Long serialNumberId);

	MemberCoupon findByMemberId(final Long memberId);

	void deleteByMemberIdAndCouponId(Long memberId, Long couponInfoId);
}
