package cart.presentation.dto.response;

import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderDto> orderDto;

    public OrderResponse(Long orderId, List<OrderDto> orderDto) {
        this.orderId = orderId;
        this.orderDto = orderDto;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderDto> getOrderInfo() {
        return orderDto;
    }
}
