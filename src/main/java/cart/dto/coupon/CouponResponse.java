package cart.dto.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.member.MemberCoupon;

public class CouponResponse {

    private Long couponId;
    private String name;
    private DiscountResponse discount;

    private CouponResponse() {
    }

    private CouponResponse(final Long couponId, final String name, final DiscountResponse discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), new DiscountResponse(coupon.getDiscount()));
    }

    public static CouponResponse from(MemberCoupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getCoupon().getName(), new DiscountResponse(coupon.getCoupon().getDiscount()));
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getName() {
        return name;
    }

    public DiscountResponse getDiscount() {
        return discount;
    }
}
