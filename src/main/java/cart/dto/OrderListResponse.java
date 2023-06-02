package cart.dto;

import cart.domain.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class OrderListResponse {

    private final List<OrderResponse> orders;
    @JsonInclude(NON_NULL)
    private final Long lastOrderId;

    public OrderListResponse(final List<OrderResponse> orders, final Long lastOrderId) {
        this.orders = orders;
        this.lastOrderId = lastOrderId;
    }

    public static OrderListResponse of(final List<Order> orders) {
        final List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());

        return new OrderListResponse(orderResponses, getLastId(orderResponses));
    }

    private static Long getLastId(final List<OrderResponse> orderResponses) {
        if (orderResponses.isEmpty()) {
            return null;
        }

        return orderResponses.get(orderResponses.size() - 1).getId();
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }

    public Long getLastOrderId() {
        return lastOrderId;
    }
}
