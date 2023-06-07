package cart.order.dto;

import cart.order.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailResponse {
    private final Long orderId;
    private final List<OrderInfoResponse> orderInfos;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;
    
    public OrderDetailResponse(final Long orderId, final List<OrderInfoResponse> orderInfos, final Long originalPrice, final Long usedPoint, final Long pointToAdd) {
        this.orderId = orderId;
        this.orderInfos = orderInfos;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }
    
    public static OrderDetailResponse from(final Order order) {
        return new OrderDetailResponse(
                order.getId(),
                getOrderInfoResponses(order),
                order.getOriginalPrice(),
                order.getUsedPoint(),
                order.getPointToAdd()
        );
    }
    
    private static List<OrderInfoResponse> getOrderInfoResponses(final Order order) {
        return order.getOrderInfos().getOrderInfos().stream()
                .map(OrderInfoResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public List<OrderInfoResponse> getOrderInfos() {
        return orderInfos;
    }
    
    public Long getOriginalPrice() {
        return originalPrice;
    }
    
    public Long getUsedPoint() {
        return usedPoint;
    }
    
    public Long getPointToAdd() {
        return pointToAdd;
    }
}
