package shop.domain.order;

import shop.domain.member.Member;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Order {
    private final Long id;
    private final List<OrderItem> orderItems;
    private final OrderPrice orderPrice;
    private final LocalDateTime orderedAt;

    public Order(Long id, List<OrderItem> orderItems,
                 OrderPrice orderPrice, LocalDateTime orderedAt) {
        this.id = id;
        this.orderItems = orderItems;
        this.orderPrice = orderPrice;
        this.orderedAt = orderedAt;
    }

    public Order(List<OrderItem> orderItems, OrderPrice orderPrice, LocalDateTime orderedAt) {
        this(null, orderItems, orderPrice, orderedAt);
    }

    public void checkOwner(Member member) {
        orderItems.forEach(orderItem -> orderItem.checkOwner(member));
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public OrderPrice getOrderPrice() {
        return orderPrice;
    }

    public int getDeliveryPrice() {
        return orderPrice.getDeliveryPrice();
    }

    public long getTotalPrice() {
        return orderPrice.getTotalPrice();
    }

    public long getDiscountedPrice() {
        return orderPrice.getDiscountedPrice();
    }
}
