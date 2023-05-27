package cart.dto;

import java.util.List;

public class OrderSaveRequest {

    private final List<ItemIdDto> orderItems;
    private final Long couponId;

    public OrderSaveRequest(final List<ItemIdDto> orderItems, final Long couponId) {
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<ItemIdDto> getOrderItems() {
        return orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }
}
