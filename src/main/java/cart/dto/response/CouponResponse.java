package cart.dto.response;

import cart.domain.Coupon;

public class CouponResponse {

    private Long couponId;
    private String name;
    private DiscountResponse discount;

    public CouponResponse() {
    }

    public CouponResponse(Long couponId, String name, DiscountResponse discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public static CouponResponse from(Coupon coupon) {
        DiscountResponse discountResponse = new DiscountResponse(coupon.getType().name(), coupon.getDiscountAmount());
        return new CouponResponse(coupon.getId(), coupon.getName(), discountResponse);
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
