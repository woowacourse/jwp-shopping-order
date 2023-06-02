package cart.member_coupon.dao;

public class MemberCouponEntity {

  private Long memberId;
  private Long couponId;

  public MemberCouponEntity(final Long memberId, final Long couponId) {
    this.memberId = memberId;
    this.couponId = couponId;
  }

  public Long getMemberId() {
    return memberId;
  }

  public Long getCouponId() {
    return couponId;
  }
}
