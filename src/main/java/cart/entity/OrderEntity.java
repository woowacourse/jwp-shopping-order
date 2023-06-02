package cart.entity;

import cart.domain.order.Order;
import cart.domain.order.OrderItem;

import java.util.List;

public class OrderEntity {

    private Long id;
    private Long memberId;
    private int deliveryFee;

    public OrderEntity(final Long id, final Long memberId, final int deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
    }

    public OrderEntity(final Long memberId, final int deliveryFee) {
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
    }

    public Order toOrder(List<OrderItem> orderItem) {
        return new Order(id, memberId, deliveryFee, orderItem);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
