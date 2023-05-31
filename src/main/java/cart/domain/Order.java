package cart.domain;

import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;

    public Order(Long id, Member member, List<OrderItem> orderItems) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
    }

    public Order(Member member, List<OrderItem> orderItems) {
        this(null, member, orderItems);
    }

    public Money calculateOriginalTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getOrderPrice)
                .reduce(new Money(0), Money::add);
    }
}
