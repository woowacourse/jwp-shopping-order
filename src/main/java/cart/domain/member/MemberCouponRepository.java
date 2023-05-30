package cart.domain.member;

public interface MemberCouponRepository {

    void save(final Long memberId, final MemberCoupon memberCoupon);

    boolean existByMemberIdAndCouponId(final Long memberId, final Long couponId);
}
