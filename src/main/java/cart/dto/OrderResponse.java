package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private LocalDateTime orderedAt;
    private List<OrderItemResponse> products;

    public OrderResponse(Long id, LocalDateTime orderedAt, List<OrderItemResponse> products) {
        this.id = id;
        this.orderedAt = orderedAt;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }
}
