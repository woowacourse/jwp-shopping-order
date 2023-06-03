package cart.domain;

public class MemberCoupon {

  private final Long id;
  private final Member member;
  private final Coupon coupon;
  private final boolean isUsed;

  public MemberCoupon(Long id, Member member, Coupon coupon, boolean isUsed) {
    this.id = id;
    this.member = member;
    this.coupon = coupon;
    this.isUsed = isUsed;
  }

  public Long getId() {
    return id;
  }

  public Member getMember() {
    return member;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public boolean isUsed() {
    return isUsed;
  }
}
