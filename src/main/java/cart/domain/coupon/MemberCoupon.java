package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.discountpolicy.DiscountPolicy;
import cart.exception.CouponException;

public class MemberCoupon {

    private final Long id;
    private final Coupon coupon;
    private boolean isUsed;
    private Money discountedPrice;

    public MemberCoupon(Coupon coupon) {
        this.id = null;
        this.coupon = coupon;
        this.isUsed = false;
        this.discountedPrice = new Money(0);
    }

    public MemberCoupon(Long id, Coupon coupon, boolean isUsed) {
        this.id = id;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.discountedPrice = null;
    }

    public MemberCoupon(Long id, Coupon coupon, boolean isUsed, Money discountedPrice) {
        this.id = id;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.discountedPrice = discountedPrice;
    }

    public Money use(Money originalPrice) {
        validateIsUsable();
        Money couponUsedPrice = coupon.apply(originalPrice);
        discountedPrice = originalPrice.subtract(couponUsedPrice);
        isUsed = true;
        return couponUsedPrice;
    }

    private void validateIsUsable() {
        if (isUsed) {
            throw new CouponException.AlreadyUsed();
        }
    }

    public Long getCouponId() {
        return coupon.getId();
    }

    public String getCouponName() {
        return coupon.getName();
    }

    public DiscountPolicy getDiscountPolicy() {
        return coupon.getDiscountPolicy();
    }

    public Long getId() {
        return id;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public Money getDiscountedPrice() {
        return discountedPrice;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
