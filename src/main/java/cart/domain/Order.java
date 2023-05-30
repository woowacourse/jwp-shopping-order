package cart.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private final Long id;
    private final Member owner;
    private final List<OrderItem> orderItems;
    private final LocalDateTime createdAt;

    public Order(Member owner, List<OrderItem> orderItems) {
        this(null, owner, orderItems);
    }

    public Order(Long id, Member owner, List<OrderItem> orderItems) {
        this(id, owner, orderItems, LocalDateTime.now());
    }

    public Order(Long id, Member owner, List<OrderItem> orderItems, LocalDateTime createdAt) {
        this.id = id;
        this.owner = owner;
        this.orderItems = new ArrayList<>(orderItems);
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Member getOwner() {
        return owner;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
