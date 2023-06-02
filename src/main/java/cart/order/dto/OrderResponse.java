package cart.order.dto;

import java.util.List;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderInfoResponse> orderInfos;
    
    public OrderResponse(final Long orderId, final List<OrderInfoResponse> orderInfos) {
        this.orderId = orderId;
        this.orderInfos = orderInfos;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public List<OrderInfoResponse> getOrderInfos() {
        return orderInfos;
    }
}
