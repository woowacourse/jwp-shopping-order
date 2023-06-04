package cart.member_coupon.domain;

import cart.coupon.domain.Coupon;
import cart.member.domain.Member;

public class MemberCoupon {

  private Member member;

  private Coupon coupon;

  private UsedStatus usedStatus;

  public MemberCoupon(final Member member, final Coupon coupon, final UsedStatus usedStatus) {
    this.member = member;
    this.coupon = coupon;
    this.usedStatus = usedStatus;
  }

  public Member getMember() {
    return member;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public UsedStatus getUsedStatus() {
    return usedStatus;
  }
}
