package cart.ui.order.dto;

import cart.application.service.order.dto.OrderResultDto;

import java.util.List;

public class OrdersResponse {

    private List<OrderResultDto> orderResponses;

    public OrdersResponse() {
    }

    public OrdersResponse(List<OrderResultDto> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public List<OrderResultDto> getOrderResponses() {
        return orderResponses;
    }
}
