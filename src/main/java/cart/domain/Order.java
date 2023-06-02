package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;
    private final LocalDateTime orderTime;

    public Order(Long id, Member member, List<OrderItem> orderItems) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        orderTime = LocalDateTime.now();
    }

    public Order(Member member, List<OrderItem> orderItems) {
        this(null, member, orderItems);
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public Money calculateOriginalTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(new Money(0), Money::add);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
