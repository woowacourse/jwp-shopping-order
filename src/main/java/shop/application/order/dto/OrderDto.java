package shop.application.order.dto;

import shop.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    private final Long id;
    private final List<OrderItemDto> orderItems;
    private final OrderPriceDto orderPrice;
    private final LocalDateTime orderedAt;

    private OrderDto(Long id, List<OrderItemDto> orderItems,
                     OrderPriceDto orderPrice, LocalDateTime orderedAt) {
        this.id = id;
        this.orderItems = orderItems;
        this.orderPrice = orderPrice;
        this.orderedAt = orderedAt;
    }

    public static OrderDto of(Order order) {
        return new OrderDto(
                order.getId(),
                OrderItemDto.of(order.getOrderItems()),
                OrderPriceDto.of(order.getOrderPrice()),
                order.getOrderedAt()
        );
    }

    public static List<OrderDto> of(List<Order> orders) {
        return orders.stream()
                .map(OrderDto::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public OrderPriceDto getOrderPrice() {
        return orderPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
