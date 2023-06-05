package cart.application.dto.response;

import java.util.List;

public class OrderDetailResponse {
    private final List<OrderItemResponse> orderItems;
    private final Integer totalPrice;
    private final Integer usedPoints;
    private final Integer orderPrice;

    public OrderDetailResponse(final List<OrderItemResponse> orderItems, final Integer totalPrice, final Integer usedPoints, final Integer orderPrice) {
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.usedPoints = usedPoints;
        this.orderPrice = orderPrice;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getUsedPoints() {
        return usedPoints;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }
}
