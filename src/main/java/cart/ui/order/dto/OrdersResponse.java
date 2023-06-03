package cart.ui.order.dto;

import cart.application.service.order.dto.OrderDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {

    private List<OrderResponse> orderResponses;

    public OrdersResponse() {
    }

    public OrdersResponse(final List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrdersResponse from(final List<OrderDto> orderDtos) {
        return new OrdersResponse(orderDtos.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList()));
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }

}
