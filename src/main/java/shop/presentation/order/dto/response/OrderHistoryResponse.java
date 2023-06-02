package shop.presentation.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryResponse {
    private Long orderId;
    private List<OrderProductResponse> items;
    private LocalDateTime orderedAt;

    private OrderHistoryResponse() {
    }

    public OrderHistoryResponse(Long orderId, List<OrderProductResponse> items, LocalDateTime orderedAt) {
        this.orderId = orderId;
        this.items = items;
        this.orderedAt = orderedAt;
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
