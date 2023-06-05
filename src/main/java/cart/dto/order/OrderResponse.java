package cart.dto.order;

import java.sql.Timestamp;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private String createdAt;
    private List<OrderItemResponse> orderItems;
    private Long totalPrice;
    private Long usedPoint;
    private Long earnedPoint;

    public OrderResponse(Long orderId, Timestamp createdAt, List<OrderItemResponse> orderItems, Long totalPrice, Long usedPoint, Long earnedPoint) {
        this.orderId = orderId;
        this.createdAt = convertToStringTime(createdAt);
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
    }

    public String convertToStringTime(Timestamp createdAt) {
        String[] time = createdAt.toString().split(" ");
        return time[0] + "T" + time[1].substring(0, 8);
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getEarnedPoint() {
        return earnedPoint;
    }
}
