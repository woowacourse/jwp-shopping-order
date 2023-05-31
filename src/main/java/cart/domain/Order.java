package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private final Point usedPoint;
    private final Point savedPoint;
    private final LocalDateTime orderedAt;

    public Order(
            final Long id,
            final List<OrderItem> orderItems,
            final int usedPoint,
            final int savedPoint,
            final LocalDateTime orderedAt
    ) {
        this.id = id;
        this.orderItems = orderItems;
        this.usedPoint = new Point(usedPoint);
        this.savedPoint = new Point(savedPoint);
        this.orderedAt = orderedAt;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Point getUsedPoint() {
        return usedPoint;
    }

    public Point getSavedPoint() {
        return savedPoint;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
