package cart.application.response;

import cart.domain.order.OrderHistory;
import cart.domain.order.OrderItems;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderWithTotalPriceResponse {

    private Long orderId;
    private List<OrderItemResponse> orderProduct;
    private int orderTotalPrice;
    private int usedPoint;
    private LocalDateTime createdAt;

    private OrderWithTotalPriceResponse(Long orderId, List<OrderItemResponse> orderProduct, int orderTotalPrice, int usedPoint, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderProduct = orderProduct;
        this.orderTotalPrice = orderTotalPrice;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public static OrderWithTotalPriceResponse from(OrderHistory orderHistory) {
        return new OrderWithTotalPriceResponse(
                orderHistory.getId(),
                collectOrderProductResponses(orderHistory.getOrderItems()),
                orderHistory.getTotalPrice().plus(orderHistory.getDeliveryFee()).getValue().intValue(),
                orderHistory.getUsePoint().getValue().intValue(),
                orderHistory.getCreatedAt()
        );
    }

    private static List<OrderItemResponse> collectOrderProductResponses(OrderItems orderItems) {
        return orderItems.getOrderItems()
                .stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderProducts() {
        return orderProduct;
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
