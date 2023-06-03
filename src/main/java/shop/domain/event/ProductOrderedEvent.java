package shop.domain.event;

import shop.domain.order.OrderItem;

import java.util.List;

public class ProductOrderedEvent {
    private final Long memberId;
    private final List<OrderItem> orderItems;

    public ProductOrderedEvent(Long memberId, List<OrderItem> orderItems) {
        this.memberId = memberId;
        this.orderItems = orderItems;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
