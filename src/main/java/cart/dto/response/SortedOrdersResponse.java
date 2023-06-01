package cart.dto.response;

import cart.domain.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.stream.Collectors;

public class SortedOrdersResponse {

    private final List<OrderResponse> orders;

    @JsonInclude(Include.NON_NULL)
    private final Long lastOrderId;

    private SortedOrdersResponse(List<OrderResponse> orders, Long lastOrderId) {
        this.orders = orders;
        this.lastOrderId = lastOrderId;
    }

    public static SortedOrdersResponse from(List<Order> orders) {
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        Long lastOrderId = calculateLastOrderId(orderResponses);

        return new SortedOrdersResponse(orderResponses, lastOrderId);
    }

    private static Long calculateLastOrderId(List<OrderResponse> orderResponses) {
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
