package cart.dto;

import java.util.List;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderInfoResponse> orderInfo;
    
    public OrderResponse(final Long orderId, final List<OrderInfoResponse> orderInfo) {
        this.orderId = orderId;
        this.orderInfo = orderInfo;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public List<OrderInfoResponse> getOrderInfo() {
        return orderInfo;
    }
}
