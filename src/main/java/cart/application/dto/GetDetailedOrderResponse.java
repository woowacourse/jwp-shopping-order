package cart.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Order;

public class GetDetailedOrderResponse {

    private final Long orderId;
    private final LocalDateTime orderAt;
    private final String orderStatus;
    private final int payAmount;
    private final int usedPoint;
    private final int savedPoint;
    private final List<SingleKindDetailedProductResponse> products;

    public GetDetailedOrderResponse(Long orderId, LocalDateTime orderAt, String orderStatus, int payAmount, int usedPoint, int savedPoint,
        List<SingleKindDetailedProductResponse> products) {
        this.orderId = orderId;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
        this.payAmount = payAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.products = products;
    }

    public static GetDetailedOrderResponse from(Order order, int increasedPoint, int usedPoint) {
        List<SingleKindDetailedProductResponse> detailedProducts = order.getQuantityAndProducts().stream()
            .map(SingleKindDetailedProductResponse::of)
            .collect(Collectors.toList());
        return new GetDetailedOrderResponse(order.getId(), order.getOrderAt(),
            order.getOrderStatus().getDisplayName(), order.getPayAmount(), usedPoint, increasedPoint,
            detailedProducts);
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public String getOrderStatus() {
        return orderStatus;
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
