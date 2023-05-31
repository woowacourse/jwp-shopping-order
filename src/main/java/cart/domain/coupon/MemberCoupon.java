package cart.domain.coupon;

import cart.domain.Member;
import java.math.BigDecimal;

public class MemberCoupon {

    private final Long id;
    private final Member member;
    private final Coupon coupon;
    private final Boolean used;

    public MemberCoupon(final Member member, final Coupon coupon, final Boolean used) {
        this(null, member, coupon, used);
    }

    public MemberCoupon(final Long id, final Member member, final Coupon coupon, final Boolean used) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.used = used;
    }

    public static MemberCoupon makeNonMemberCoupon() {
        return new MemberCoupon(null, null, Coupon.makeNonDiscountPolicyCoupon(), false);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getCouponName() {
        return coupon.getName();
    }

    public String getCouponType() {
        return coupon.getType();
    }

    public Long getDiscountPrice() {
        return coupon.getDiscountPolicy().getDiscountPrice();
    }

    public Long getMinimumPrice() {
        return coupon.getMinimumPrice();
    }

    public Boolean getUsed() {
        return used;
    }

    public BigDecimal discountPrice(final Long totalPrice) {
        return coupon.apply(totalPrice);
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
