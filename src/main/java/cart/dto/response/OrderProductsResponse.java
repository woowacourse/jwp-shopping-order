package cart.dto.response;

import java.util.List;

public class OrderProductsResponse {
    private final List<OrderProductResponse> orderItems;
    private final Integer originalPrice;
    private final Integer usedPoints;
    private final Integer orderPrice;

    public OrderProductsResponse(
            final List<OrderProductResponse> orderItems,
            final Integer originalPrice,
            final Integer usedPoints,
            final Integer orderPrice
    ) {
        this.orderItems = orderItems;
        this.originalPrice = originalPrice;
        this.usedPoints = usedPoints;
        this.orderPrice = orderPrice;
    }

    public List<OrderProductResponse> getOrderItems() {
        return orderItems;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getUsedPoints() {
        return usedPoints;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }
}
