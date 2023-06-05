package cart.domain.memberCoupon;

import cart.domain.Model;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountAction;
import cart.domain.member.Member;

import java.util.Objects;

public class MemberCoupon implements Model, DiscountAction {
    private final Long id;
    private final Member member;
    private final Coupon coupon;

    public MemberCoupon(Long id, Member member, Coupon coupon) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberCoupon that = (MemberCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int discount(int money) {
        return coupon.discount(money);
    }
}
