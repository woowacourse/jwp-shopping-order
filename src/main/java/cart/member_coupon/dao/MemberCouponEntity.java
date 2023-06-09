package cart.member_coupon.dao;

public class MemberCouponEntity {

  private Long memberId;
  private Long couponId;
  private String usedYn;

  public MemberCouponEntity(final Long memberId, final Long couponId, final String usedYn) {
    this.memberId = memberId;
    this.couponId = couponId;
    this.usedYn = usedYn;
  }

  public Long getMemberId() {
    return memberId;
  }

  public Long getCouponId() {
    return couponId;
  }

  public String getUsedYn() {
    return usedYn;
  }
}
