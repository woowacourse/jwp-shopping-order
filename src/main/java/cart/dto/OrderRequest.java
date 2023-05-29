package cart.dto;

import cart.dto.OrderItem;

import java.util.List;

public class OrderRequest {
    private final List<OrderItem> orderItems;
    private final Long couponId;

    public OrderRequest(final List<OrderItem> orderItems, final Long couponId) {
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }
}
