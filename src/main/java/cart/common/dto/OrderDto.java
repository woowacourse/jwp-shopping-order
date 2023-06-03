package cart.common.dto;

import cart.order.application.OrderItem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    private final Long orderId;
    private final String orderDateTime;
    private final List<OrderItemDto> orderItems;
    private final int totalPrice;

    public OrderDto(Long orderId, String orderDateTime, List<OrderItemDto> orderItems, int totalPrice) {
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public static OrderDto of(Long orderId, LocalDateTime orderDateTime, List<OrderItem> orderItems, int totalPrice) {
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new OrderDto(orderId, orderDateTime.format(formatter), orderItemDtos, totalPrice);
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
