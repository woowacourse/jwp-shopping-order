package cart.dto;

import java.util.List;

public class OrderSaveRequest {

    private final List<Long> orderItems;
    private final Long couponId;

    public OrderSaveRequest(final List<Long> orderItems, final Long couponId) {
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<Long> getOrderItems() {
        return orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }

}
