package cart.domain.member;

public interface MemberCouponRepository {

    void save(final Long memberId, final MemberCoupon memberCoupon);

    boolean existByMemberIdAndCouponId(final Long memberId, final Long couponId);

    MemberCoupon findByMemberIdAndCouponId(final Long memberId, final Long couponId);

    void updateUsed(final Long memberId, final Long couponId);

    void updateNotUsed(final Long memberId, final Long couponId);
}
