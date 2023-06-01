package cart.ui.order;

import cart.ui.order.dto.OrderResponse;

import java.util.List;

public class OrdersResponse {

    private List<OrderResponse> orderResponses;

    public OrdersResponse() {
    }

    public OrdersResponse(final List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }

}
