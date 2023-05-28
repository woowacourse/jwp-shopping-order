package cart.dto;

import java.util.List;

public class OrderSaveRequest {

    private final List<OrderItemIdDto> orderItems;
    private final Long couponId;

    public OrderSaveRequest(final List<OrderItemIdDto> orderItems, final Long couponId) {
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<OrderItemIdDto> getOrderItems() {
        return orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }

}
