package cart.presentation.dto.response;

import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderInfo> orderInfo;

    public OrderResponse(Long orderId, List<OrderInfo> orderInfo) {
        this.orderId = orderId;
        this.orderInfo = orderInfo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderInfo> getOrderInfo() {
        return orderInfo;
    }
}
