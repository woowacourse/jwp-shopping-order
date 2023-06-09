package cart.dto.response;

import cart.domain.Coupon;
import cart.domain.MemberCoupon;

public class OrderCouponResponse {

    private Long couponId;
    private String name;
    private DiscountResponse discount;

    public OrderCouponResponse() {
    }

    public OrderCouponResponse(Long couponId, String name, DiscountResponse discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public static OrderCouponResponse from(MemberCoupon memberCoupon) {
        Coupon coupon = memberCoupon.getCoupon();
        return new OrderCouponResponse(memberCoupon.getId(), coupon.getName(),
                new DiscountResponse(coupon.getType().name(), coupon.getDiscountAmount()));
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
