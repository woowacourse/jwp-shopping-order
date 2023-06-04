package cart.dto.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderResponse {

    private long orderId;
    private List<OrderProductResponse> orderProducts;
    private int orderTotalPrice;
    private int usedPoint;
    private String createdAt;

    public OrderResponse() {
    }

    public OrderResponse(long orderId, List<OrderProductResponse> orderProducts, int orderTotalPrice, int usedPoint, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
        this.orderTotalPrice = orderTotalPrice;
        this.usedPoint = usedPoint;
        this.createdAt = convert(createdAt);
    }

    public OrderResponse(long orderId, List<OrderProductResponse> orderProducts, int orderTotalPrice, int usedPoint, String createdAt) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
        this.orderTotalPrice = orderTotalPrice;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    private String convert(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }

    public int getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
