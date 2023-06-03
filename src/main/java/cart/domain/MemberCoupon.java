package cart.domain;

public class MemberCoupon {

    private final Long id;
    private final Member member;
    private final Coupon coupon;

    public MemberCoupon(final Long id, final Member member, final Coupon coupon) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
    }

    public boolean isSameMember(MemberCoupon otherMemberCoupon) {
        return this.member
                .getEmail()
                .equals(otherMemberCoupon.getMember().getEmail());
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

    @Override
    public String toString() {
        return "MemberCoupon{" +
                "id=" + id +
                ", member=" + member +
                ", coupon=" + coupon +
                '}';
    }

}
