package cart.ui.order.dto.response;

import cart.application.service.order.dto.OrderResultDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {

    private List<OrderResponse> orderResponses;

    public OrdersResponse() {
    }

    public OrdersResponse(final List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrdersResponse from(final List<OrderResultDto> orderDtos) {
        return new OrdersResponse(orderDtos.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList()));
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }

}
