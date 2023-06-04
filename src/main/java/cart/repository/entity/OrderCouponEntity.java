package cart.repository.entity;

import cart.domain.coupon.MemberCoupon;

public class OrderCouponEntity {

    private Long id;
    private String couponName;
    private Integer discountAmount;
    private Long memberCouponId;
    private Long orderId;

    public OrderCouponEntity(Long id, String couponName, Integer discountAmount, Long memberCouponId, Long orderId) {
        this.id = id;
        this.couponName = couponName;
        this.discountAmount = discountAmount;
        this.memberCouponId = memberCouponId;
        this.orderId = orderId;
    }

    public static OrderCouponEntity of(MemberCoupon memberCoupon, Long orderId) {
        return new OrderCouponEntity(
                null,
                memberCoupon.getCouponName(),
                memberCoupon.getDiscountedPrice().getValue(),
                memberCoupon.getCouponId(),
                orderId
        );
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
