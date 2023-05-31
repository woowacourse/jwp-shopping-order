package cart.dto;

import cart.domain.order.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersSelectResponse {
    private List<OrderSimpleInfoResponse> orders;

    private OrdersSelectResponse() {
    }

    private OrdersSelectResponse(final List<OrderSimpleInfoResponse> orders) {
        this.orders = orders;
    }

    public static OrdersSelectResponse from(final List<Order> orders) {
        final List<OrderSimpleInfoResponse> orderSimpleInfo = orders.stream()
                .map(order -> OrderSimpleInfoResponse.from(order))
                .collect(Collectors.toUnmodifiableList());
        return new OrdersSelectResponse(orderSimpleInfo);
    }

    public List<OrderSimpleInfoResponse> getOrders() {
        return orders;
    }
}
