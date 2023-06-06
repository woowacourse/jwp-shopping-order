package cart.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Order;

public class OrderResponse {

    private final Long orderId;
    private final String createdAt;
    private final List<OrderItemResponse> orderItems;
    private final int totalPrice;
    private final int usedPoint;
    private final int earnedPoint;

    public OrderResponse(final Long orderId, final String createdAt, final List<OrderItemResponse> orderItems, final int totalPrice, final int usedPoint, final int earnedPoint) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
    }

    public static OrderResponse of(final Order order) {
        return new OrderResponse(
                order.getId(),
                format(order.getCreatedAt()),
                order.getCartItems().getCartItems().stream().map(OrderItemResponse::of).collect(Collectors.toList()),
                order.getCartItems().getTotalPrice().getValue(),
                order.getOrderPoint().getUsedPoint().getValue(),
                order.getOrderPoint().getEarnedPoint().getValue()
        );
    }

    private static String format(final Timestamp timestamp) {
        final LocalDateTime localDateTime = timestamp.toLocalDateTime();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
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
