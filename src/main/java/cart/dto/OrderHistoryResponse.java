package cart.dto;

import cart.domain.OrderEntity;

import java.util.List;

public class OrderHistoryResponse {
    private final Long id;
    private final String time;
    private final List<OrderItemHistoryResponse> orderItemHistoryResponses;

    public OrderHistoryResponse(Long id, String time, List<OrderItemHistoryResponse> orderItemHistoryResponses) {
        this.id = id;
        this.time = time;
        this.orderItemHistoryResponses = orderItemHistoryResponses;
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public List<OrderItemHistoryResponse> getOrderItemHistoryResponses() {
        return orderItemHistoryResponses;
    }
}
