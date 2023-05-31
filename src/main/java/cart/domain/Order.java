package cart.domain;

import cart.exception.IllegalOrderException;
import java.sql.Timestamp;
import java.util.List;

public class Order {

    private Long id;
    private final List<OrderItem> items;
    private final Timestamp createdAt;

    public Order(List<OrderItem> items) {
        this(items, null);
    }

    public Order(List<OrderItem> items, Timestamp createdAt) {
        this(null, items, createdAt);
    }

    public Order(Long id, List<OrderItem> items, Timestamp createdAt) {
        validate(items);
        this.id = id;
        this.items = items;
        this.createdAt = createdAt;
    }

    private void validate(List<OrderItem> items) {
        if (items.isEmpty()) {
            throw new IllegalOrderException("주문 상품은 비어있을 수 없습니다.");
        }
    }

    public Money calculateTotalPrice() {
        return items.stream()
            .map(OrderItem::calculatePrice)
            .reduce(Money.MIN, Money::add);
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
