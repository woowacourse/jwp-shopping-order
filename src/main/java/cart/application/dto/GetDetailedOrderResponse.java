package cart.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GetDetailedOrderResponse {

    private final Long orderId;
    private final LocalDateTime orderAt;
    private final int payAmount;
    private final int usedPoint;
    private final int savedPoint;
    private final List<SingleKindDetailedProductResponse> products;

    public GetDetailedOrderResponse(Long orderId, LocalDateTime orderAt, int payAmount, int usedPoint, int savedPoint,
        List<SingleKindDetailedProductResponse> products) {
        this.orderId = orderId;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.products = products;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    public List<SingleKindDetailedProductResponse> getProducts() {
        return products;
    }
}
