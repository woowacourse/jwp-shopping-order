package cart.dto;

import cart.domain.Order;
import cart.domain.OrderInfo;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderInfoResponse> orderInfo;
    private final int originalPrice;
    private final int usedPoint;
    private final int pointToAdd;

    private OrderResponse(Long orderId, List<OrderInfoResponse> orderInfo, int originalPrice, int usedPoint, int pointToAdd) {
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public static OrderResponse of(Order order, List<OrderInfo> orderInfos) {
        return new OrderResponse(order.getId(),
                orderInfos.stream()
                        .map(OrderInfoResponse::from)
                        .collect(Collectors.toList()),
                order.getOriginalPrice().intValue(),
                order.getUsedPoint().intValue(),
                order.getPointToAdd().intValue());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderInfoResponse> getOrderInfo() {
        return orderInfo;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getPointToAdd() {
        return pointToAdd;
    }
}
