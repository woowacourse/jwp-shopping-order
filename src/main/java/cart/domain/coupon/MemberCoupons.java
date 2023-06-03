package cart.domain.coupon;

import cart.domain.member.Member;

import java.util.List;

public class MemberCoupons {

    private final Member member;
    private final Coupons coupons;

    public MemberCoupons(final Member member, final Coupons coupons) {
        this.member = member;
        this.coupons = coupons;
    }

    public Member getMember() {
        return member;
    }

    public Coupons getCoupons() {
        return coupons;
    }

    public void validateHasCoupons(final List<Long> couponIds) {
        coupons.validateHasCoupons(couponIds);
    }
}
