package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;

public class MemberCoupon {

    private final Long id;
    private final Member member;
    private final Coupon coupon;
    private boolean used;

    public MemberCoupon(final Member member, final Coupon coupon) {
        this(null, member, coupon, false);
    }

    public MemberCoupon(final Long id, final Member member, final Coupon coupon, final boolean used) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.used = used;
    }

    public void use() {
        used = true;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Long getCouponId() {
        return coupon.getId();
    }

    public boolean isUsed() {
        return used;
    }
}
