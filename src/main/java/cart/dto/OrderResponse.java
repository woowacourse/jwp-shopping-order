package cart.dto;

import cart.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long orderId;
    private CouponResponse coupon;
    private List<CartItemResponse> items;
    private int totalPrice;
    private int discountedTotalPrice;
    private int couponDiscountPrice;
    private int deliveryPrice;
    private LocalDateTime orderedAt;

    public OrderResponse(final Order order) {
        this.orderId = order.getId();
        if (order.getCoupon() == null) {
            this.coupon = new CouponResponse();
        }
        if (order.getCoupon() != null) {
            this.coupon = new CouponResponse(order.getCoupon());
        }
        this.items = order.getItems().getItems()
                .stream().map(CartItemResponse::of)
                .collect(Collectors.toUnmodifiableList());
        this.totalPrice = order.getTotalPrice();
        this.discountedTotalPrice = order.getDiscountedTotalPrice();
        this.couponDiscountPrice = order.getCouponDiscountPrice();
        this.deliveryPrice = order.getDeliveryPrice();
        this.orderedAt = order.getOrderedAt();
    }

    public Long getOrderId() {
        return orderId;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }

    public List<CartItemResponse> getItems() {
        return items;
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
}
