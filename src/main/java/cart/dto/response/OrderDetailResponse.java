package cart.dto.response;

import cart.domain.OrderHistory;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailResponse {

    private List<OrderItemDetailResponse> orderItems;
    private Integer originalPrice;
    private Integer usedPoints;
    private Integer orderPrice;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(final List<OrderItemDetailResponse> orderItems, final Integer originalPrice,
                               final Integer usedPoints,
                               final Integer orderPrice) {
        this.orderItems = orderItems;
        this.originalPrice = originalPrice;
        this.usedPoints = usedPoints;
        this.orderPrice = orderPrice;
    }

    public static OrderDetailResponse of(OrderHistory orderHistory) {
        List<OrderItemDetailResponse> orderItems = orderHistory.getOrderItems().stream()
                .map(OrderItemDetailResponse::of)
                .collect(Collectors.toList());
        return new OrderDetailResponse(orderItems, orderHistory.getOriginalPrice(), orderHistory.getUsedPoint(),
                orderHistory.getOrderPrice());
    }

    public List<OrderItemDetailResponse> getOrderItems() {
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
