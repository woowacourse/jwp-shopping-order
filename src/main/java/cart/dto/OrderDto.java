package cart.dto;

import cart.domain.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    private final Long orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderDateTime;
    private final List<OrderItemDto> orderItems;
    private final int totalPrice;

    public OrderDto(Long orderId, LocalDateTime orderDateTime, List<OrderItemDto> orderItems, int totalPrice) {
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public static OrderDto of(Long orderId, LocalDateTime orderDateTime, List<OrderItem> orderItems, int totalPrice) {
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
        return new OrderDto(orderId, orderDateTime, orderItemDtos, totalPrice);
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
