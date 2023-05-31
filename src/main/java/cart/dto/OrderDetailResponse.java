package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {

    private final Long id;
    private final int usedPoint;
    private final int savedPoint;
    private final LocalDateTime orderedAt;
    private final List<OrderItemResponse> products;

    public OrderDetailResponse(
            final Long id,
            final int usedPoint,
            final int savedPoint,
            final LocalDateTime orderedAt,
            final List<OrderItemResponse> products
    ) {
        this.id = id;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderedAt = orderedAt;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }
}
