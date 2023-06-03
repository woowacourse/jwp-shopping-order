package cart.dto.order;

import cart.domain.order.Order;
import cart.dto.cart.CartItemResponse;
import cart.dto.coupon.OrderCouponResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long orderId;
    private OrderCouponResponse coupon;
    private List<CartItemResponse> items;
    private int totalPrice;
    private int discountedTotalPrice;
    private int couponDiscountPrice;
    private int deliveryPrice;
    private LocalDateTime orderedAt;

    public OrderResponse(final Order order) {
        this.orderId = order.getId();
        if (order.getCoupon().isEmpty()) {
            this.coupon = new OrderCouponResponse();
        }
        if (order.getCoupon().isPresent()) {
            this.coupon = new OrderCouponResponse(order.getCoupon().get());
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

    public OrderCouponResponse getCoupon() {
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
