package cart.domain;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Money;

public class MemberCoupon {
    private final Long id;
    private final Member member;
    private final Coupon coupon;
    private final String expiredAt;

    public MemberCoupon(Long id, Member member, Coupon coupon, String expiredAt) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.expiredAt = expiredAt;
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

    public String getExpiredAt() {
        return expiredAt;
    }

    public boolean isAvailable(Money price) {
        return coupon.isAvailable(price);
    }
}
