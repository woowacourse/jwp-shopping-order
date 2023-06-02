package cart.presentation.dto.response;

import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderDto> orderInfos;

    public OrderResponse(Long orderId, List<OrderDto> orderInfos) {
        this.orderId = orderId;
        this.orderInfos = orderInfos;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderDto> getOrderInfos() {
        return orderInfos;
    }
}
