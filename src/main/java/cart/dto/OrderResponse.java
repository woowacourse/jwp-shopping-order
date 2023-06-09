package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderProductResponse> products;
    private final int totalPrice;
    private final int usedPoint;
    private final int deliveryFee;
    private final LocalDateTime orderedAt;

    public OrderResponse(final Long orderId, final List<OrderProductResponse> products, final int totalPrice,
                         final int usedPoint, final int deliveryFee,
                         final LocalDateTime orderedAt) {
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
