package cart.dto;

import cart.domain.OrderDetail;
import cart.domain.Orders;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {

    private Long orderId;
    private LocalDateTime createdAt;
    private List<OrderProductResponse> orderItems;
    private int totalPrice;
    private int usedPoint;
    private int earnedPoint;

    private OrderDetailResponse(Long orderId, LocalDateTime createdAt, List<OrderProductResponse> orderItems, int totalPrice, int usedPoint, int earnedPoint) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
    }

    public static OrderDetailResponse of(Orders orders, List<OrderDetail> orderDetails) {
        List<OrderProductResponse> orderProductResponses = OrderProductResponse.createOrderProductResponses(orderDetails);
        int totalPrice = orderProductResponses.stream().map(orderProductResponse -> orderProductResponse.getPrice() * orderProductResponse.getQuantity()).reduce(Integer::sum).orElseThrow(() -> new IllegalArgumentException("총 결제 금액 계산 실패"));

        return new OrderDetailResponse(orders.getId(), orders.getCreatedAt(), orderProductResponses, totalPrice, orders.getUsedPoint(), orders.getEarnedPoint());
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderProductResponse> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getEarnedPoint() {
        return earnedPoint;
    }
}
