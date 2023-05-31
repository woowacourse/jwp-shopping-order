package cart.domain;

import cart.exception.IllegalOrderException;
import java.util.List;

public class Order {

    private Long id;
    private List<OrderItem> items;

    public Order(List<OrderItem> items) {
        this(null, items);
    }

    public Order(Long id, List<OrderItem> items) {
        validate(items);
        this.id = id;
        this.items = items;
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
}
