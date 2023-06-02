package cart.domain;

public class MemberCoupon {

    private final Long id;
    private final Member member; // TODO : Member 로 확인하는 기능 추가 (현재 해당 MemberCoupon 을 호출한 인원이 주인이 맞는지))
    private final Coupon coupon;

    public MemberCoupon(final Long id, final Member member, final Coupon coupon) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
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
