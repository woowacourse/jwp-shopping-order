package cart.dto;

import java.util.List;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderInfo> orderInfo;
    private final int originalPrice;
    private final int usedPoint;
    private final int pointToAdd;

    public OrderResponse(Long orderId, List<OrderInfo> orderInfo, int originalPrice, int usedPoint, int pointToAdd) {
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderInfo> getOrderInfo() {
        return orderInfo;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getPointToAdd() {
        return pointToAdd;
    }
}
