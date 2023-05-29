package cart.domain;

import java.awt.Point;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private List<OrderItem> orderItems;
    private LocalDateTime createdAt;
    private Point spendPoint;

    public Order(Long id, List<OrderItem> orderItems, LocalDateTime createdAt, Point spendPoint) {
        this.id = id;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
        this.spendPoint = spendPoint;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Point getSpendPoint() {
        return spendPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
