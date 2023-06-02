package shop.ui.order.dto.request;

import java.util.List;

public class OrderCreationRequest {
    private List<OrderItemRequest> items;
    private Long couponId;

    private OrderCreationRequest() {
    }

    public OrderCreationRequest(List<OrderItemRequest> items, Long couponId) {
        this.items = items;
        this.couponId = couponId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public Long getCouponId() {
        return couponId;
    }
}
