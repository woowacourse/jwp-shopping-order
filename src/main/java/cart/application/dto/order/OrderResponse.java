package cart.application.dto.order;

import cart.application.dto.coupon.CouponResponse;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final int totalPrice;
    private final int discountedTotalPrice;
    private final int couponDiscountPrice;
    private final int deliveryPrice;
    private final LocalDateTime orderedAt;
    private final CouponResponse coupon;
    private final List<OrderProductResponse> items;
    private final boolean isValid;

    public OrderResponse(final Long orderId, final int totalPrice, final int discountedTotalPrice,
                         final int couponDiscountPrice, final int deliveryPrice,
                         final LocalDateTime orderedAt, final CouponResponse coupon,
                         final List<OrderProductResponse> items,
                         final boolean isValid) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.discountedTotalPrice = discountedTotalPrice;
        this.couponDiscountPrice = couponDiscountPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderedAt = orderedAt;
        this.coupon = coupon;
        this.items = items;
        this.isValid = isValid;
    }

    public Long getOrderId() {
        return orderId;
    }


    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public int getCouponDiscountPrice() {
        return couponDiscountPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }

    public List<OrderProductResponse> getItems() {
        return items;
    }

    public boolean getIsValid() {
        return isValid;
    }
}
