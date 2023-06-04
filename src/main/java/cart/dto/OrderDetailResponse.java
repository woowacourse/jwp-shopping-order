package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {
    private final Long id;
    private final LocalDateTime orderedAt;
    private final int usedPoint;
    private final int savedPoint;
    private final List<OrderItemResponse> products;

    public OrderDetailResponse(Long id, LocalDateTime orderedAt, int usedPoint, int savedPoint,
                               List<OrderItemResponse> products) {
        this.id = id;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }
}
