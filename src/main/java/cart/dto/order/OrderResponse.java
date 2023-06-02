package cart.dto.order;

import cart.domain.order.OrderProducts;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class OrderResponse {

    private final long orderId;
    private final OrderProducts orderProducts;
    private final int orderTotalPrice;
    private final int usedPoint;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public OrderResponse(long orderId, OrderProducts orderProducts, int orderTotalPrice, int usedPoint, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
        this.orderTotalPrice = orderTotalPrice;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public long getOrderId() {
        return orderId;
    }

    public OrderProducts getOrderProducts() {
        return orderProducts;
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
