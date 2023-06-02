package shop.presentation.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {
    private Long orderId;
    private UsingCouponResponse coupon;
    private List<OrderProductResponse> items;
    private Long totalPrice;
    private Long discountedTotalPrice;
    private Long couponDiscountPrice;
    private Integer deliveryPrice;
    private LocalDateTime orderedAt;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(Long orderId, UsingCouponResponse coupon, List<OrderProductResponse> items,
                               Long totalPrice, Long discountedTotalPrice, Long couponDiscountPrice,
                               Integer deliveryPrice, LocalDateTime orderedAt) {
        this.orderId = orderId;
        this.coupon = coupon;
        this.items = items;
        this.totalPrice = totalPrice;
        this.discountedTotalPrice = discountedTotalPrice;
        this.couponDiscountPrice = couponDiscountPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderedAt = orderedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public UsingCouponResponse getCoupon() {
        return coupon;
    }

    public List<OrderProductResponse> getItems() {
        return items;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public Long getCouponDiscountPrice() {
        return couponDiscountPrice;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
