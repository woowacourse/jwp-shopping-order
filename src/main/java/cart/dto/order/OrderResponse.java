package cart.dto.order;

import cart.domain.OrderItems;

import java.sql.Timestamp;

public class OrderResponse {

    private final long id;
    private final OrderItems orderItems;
    private final int orderTotalPrice;
    private final int usedPoint;
    private final Timestamp createdAt;

    public OrderResponse(long id, OrderItems orderItems, int orderTotalPrice, int usedPoint, Timestamp createdAt) {
        this.id = id;
        this.orderItems = orderItems;
        this.orderTotalPrice = orderTotalPrice;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public int getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
