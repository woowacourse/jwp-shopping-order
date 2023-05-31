package cart.domain.memberCoupon;

import cart.domain.Member;
import cart.domain.coupon.Coupon;

public class MemberCoupon {
    private final Long id;
    private final Member member;
    private final Coupon coupon;

    public MemberCoupon(Long id, Member member, Coupon coupon) {
        this.id = id; // TODO UseState, 유효기간 추가
        this.member = member;
        this.coupon = coupon;
    }

    public MemberCoupon(Member member, Coupon coupon) {
        this(null, member, coupon);
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
}
