package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {
    private Long id;
    private Long usedPoint;
    private Long savedPoint;
    private LocalDateTime orderedAt;
    private List<OrderItemResponse> products;

    public OrderDetailResponse(Long id, Long usedPoint, Long savedPoint, LocalDateTime orderedAt, List<OrderItemResponse> products) {
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

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }
}
