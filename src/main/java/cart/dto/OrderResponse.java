package cart.dto;

import cart.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final Long usedPoint;
    private final LocalDateTime orderedAt;
    private final List<OrderedItemResponse> products;

    private Long savedPoint;

    public OrderResponse(Long id, Long usedPoint, Long savedPoint, LocalDateTime orderedAt, List<OrderedItemResponse> products) {
        this.id = id;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderedAt = orderedAt;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        List<OrderedItemResponse> orderedItemResponses = order.getOrderedItems().getOrderedItems().stream()
                .map(OrderedItemResponse::of)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getUsedPoint().getValue(),
                order.getSavedPoint().getValue(), order.getOrderedAt(), orderedItemResponses);
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

    public List<OrderedItemResponse> getProducts() {
        return products;
    }
}
