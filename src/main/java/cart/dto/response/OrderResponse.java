package cart.dto.response;

import cart.domain.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final List<OrderItemResponse> orderItems;
    private final int totalPrice;
    private final int payPrice;
    private final int earnedPoints;
    private final int usedPoints;

    @JsonFormat(pattern = "yyyy-MM-dd hh:MM:ss")
    private final LocalDateTime orderDate;

    private OrderResponse(
            Long id,
            List<OrderItemResponse> orderItems,
            int totalPrice,
            int payPrice,
            int earnedPoints,
            int usedPoints,
            LocalDateTime orderDate
    ) {
        this.id = id;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.earnedPoints = earnedPoints;
        this.usedPoints = usedPoints;
        this.orderDate = orderDate;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                orderItemResponses,
                order.calculateTotalPrice().getValue(),
                order.calculatePayPrice().getValue(),
                order.getEarnedPoints().getValue(),
                order.getUsedPoints().getValue(),
                order.getOrderDate()
        );
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPayPrice() {
        return payPrice;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
