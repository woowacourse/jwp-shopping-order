package cart.application.dto.order;

import cart.domain.order.Order;
import java.util.List;
import java.util.stream.Collectors;

public class FindOrdersResponse {

    private List<OrderResponse> orders;

    public FindOrdersResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static FindOrdersResponse from(final List<Order> orders) {
        return new FindOrdersResponse(orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList()));
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
