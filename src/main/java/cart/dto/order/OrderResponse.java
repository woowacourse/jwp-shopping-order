package cart.dto.order;

import cart.domain.OrderItems;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class OrderResponse {

    private final long id;
    private final OrderItems orderItems;
    private final int orderTotalPrice;
    private final int usedPoint;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public OrderResponse(long id, OrderItems orderItems, int orderTotalPrice, int usedPoint, LocalDateTime createdAt) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
