package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;
    private final LocalDateTime orderTime;

    public Order(final Member member, final List<OrderItem> orderItems) {
        this(null, member, orderItems);
    }

    public Order(final Long id, final Member member, final List<OrderItem> orderItems) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.orderTime = LocalDateTime.now();
    }

    public Order(final Long id, final Member member, final List<OrderItem> orderItems, final LocalDateTime orderTime) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.orderTime = orderTime;
    }

    public LocalDateTime getOrderTime() {
        return this.orderTime;
    }

    public Money calculateOriginalTotalPrice() {
        return this.orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(new Money(0), Money::add);
    }

    public Long getId() {
        return this.id;
    }

    public Member getMember() {
        return this.member;
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }
}
