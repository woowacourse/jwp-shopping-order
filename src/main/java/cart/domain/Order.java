package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private final Point usedPoint;
    private final Point savedPoint;
    private final LocalDateTime orderedAt;

    public Order(final List<OrderItem> orderItems,
                 final Point usedPoint,
                 final Point savedPoint) {
        this(null, orderItems, usedPoint, savedPoint, null);
    }

    public Order(
            final Long id,
            final List<OrderItem> orderItems,
            final int usedPoint,
            final int savedPoint,
            final LocalDateTime orderedAt
    ) {
        this(id, orderItems, new Point(usedPoint), new Point(savedPoint), orderedAt);
    }

    public Order(
            final Long id,
            final List<OrderItem> orderItems,
            final Point usedPoint,
            final Point savedPoint,
            final LocalDateTime orderedAt
    ) {
        this.id = id;
        this.orderItems = orderItems;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderedAt = orderedAt;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getUsedPoint() {
        return usedPoint.getValue();
    }

    public int getSavedPoint() {
        return savedPoint.getValue();
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
