package cart.coupon.domain;

import cart.member.domain.Member;

public class MemberCoupon {

  private final Member member;

  private final Coupon coupon;

  private UsedStatus usedStatus;

  public MemberCoupon(final Member member, final Coupon coupon, final UsedStatus usedStatus) {
    this.member = member;
    this.coupon = coupon;
    this.usedStatus = usedStatus;
  }

  public void changeUsedStatus() {
    usedStatus = usedStatus.opposite();
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
