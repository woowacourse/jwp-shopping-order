package cart.domain2.cart;

import cart.domain2.common.Money;
import cart.domain2.coupon.Coupon;

public class MemberCoupon {

    private final Long id;
    private final Long memberId;
    private final Coupon coupon;
    private boolean used;

    public MemberCoupon(final Long memberId, final Coupon coupon) {
        this(null, memberId, coupon, false);
    }

    public MemberCoupon(final Long id, final Long memberId, final Coupon coupon, final boolean used) {
        this.id = id;
        this.memberId = memberId;
        this.coupon = coupon;
        this.used = used;
    }

    public static MemberCoupon empty(final Long memberId) {
        return new MemberCoupon(memberId, Coupon.EMPTY);
    }

    public boolean isInvalidCoupon(final Money price) {
        return coupon.isInvalidPrice(price);
    }

    public Money calculatePrice(final Money price) {
        return coupon.calculatePrice(price);
    }

    public Money calculateDeliveryFee(final Money deliveryFee) {
        return coupon.calculateDeliveryFee(deliveryFee);
    }

    public void use() {
        used = true;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
