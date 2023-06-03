package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private final Long id;
    private final Long usedPoint;
    private final LocalDateTime orderedAt;
    private final List<OrderItemResponse> products;

    private Long savedPoint;

    public OrderResponse(Long id, Long usedPoint, Long savedPoint, LocalDateTime orderedAt, List<OrderItemResponse> products) {
        this.id = id;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderedAt = orderedAt;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getSavedPoint() {
        return savedPoint;
    }

    public void setSavedPoint(Long savedPoint) {
        this.savedPoint = savedPoint;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }
}
