package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderedItems;
    private final LocalDateTime createdAt;
    private final Member member;

    public Order(
            final Long id,
            final List<OrderItem> orderedItems,
            final LocalDateTime createdAt,
            final Member member
    ) {
        this.id = id;
        this.orderedItems = orderedItems;
        this.createdAt = createdAt;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderedItems() {
        return orderedItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Member getMember() {
        return member;
    }
}
