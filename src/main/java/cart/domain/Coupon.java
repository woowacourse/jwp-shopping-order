package cart.domain;

import cart.dao.entity.CouponEntity;
import cart.exception.CouponException;

public class Coupon {

    private final Long id;
    private final Member member;
    private final CouponType couponType;
    private final boolean isUsed;

    private Coupon(final Long id, final Member member, final CouponType couponType, final boolean isUsed) {
        this.id = id;
        this.member = member;
        this.couponType = couponType;
        this.isUsed = isUsed;
    }

    public Coupon(final Long id, final Member member) {
        this(id, member, null, false);
    }

    public static Coupon of(final CouponEntity coupon, final CouponType couponType) {
        return new Coupon(coupon.getId(),
                new Member(coupon.getMemberId()),
                couponType,
                coupon.isUsed());
    }

    public Money discount(final Money price) {
        if (couponType.getDiscountType() == DiscountType.DISCOUNT_PRICE) {
            return price.minus(couponType.getDiscountAmount());
        }
        throw new IllegalStateException("no discount type defined");
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public Coupon use(final Member member) {
        checkMember(member);
        if (isUsed) {
            throw new CouponException.AlreadyUsed(id);
        }
        return new Coupon(id, member, couponType, true);
    }

    public Coupon refund(final Member member) {
        checkMember(member);
        if (!isUsed) {
            throw new CouponException.AlreadyUsable(id);
        }
        return new Coupon(id, member, couponType, false);
    }

    private void checkMember(final Member member) {
        if (this.member.getId() != member.getId()) {
            throw new CouponException.IllegalMember(this, member);
        }
    }
}
