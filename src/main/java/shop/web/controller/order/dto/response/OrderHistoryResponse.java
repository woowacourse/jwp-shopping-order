package shop.web.controller.order.dto.response;

import shop.application.order.dto.OrderDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderHistoryResponse {
    private Long orderId;
    private List<OrderProductResponse> items;
    private LocalDateTime orderedAt;

    private OrderHistoryResponse() {
    }

    private OrderHistoryResponse(Long orderId, List<OrderProductResponse> items, LocalDateTime orderedAt) {
        this.orderId = orderId;
        this.items = items;
        this.orderedAt = orderedAt;
    }

    public static OrderHistoryResponse of(OrderDto order) {
        return new OrderHistoryResponse(
                order.getId(),
                OrderProductResponse.of(order.getOrderItems()),
                order.getOrderedAt()
        );
    }

    public static List<OrderHistoryResponse> of(List<OrderDto> orders) {
        return orders.stream()
                .map(OrderHistoryResponse::of)
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getItems() {
        return items;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
