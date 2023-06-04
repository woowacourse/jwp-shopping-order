package cart.dto.response;

import cart.domain.OrderHistory;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemsResponse {

    List<OrderItemResponse> orderItems;

    public OrderItemsResponse() {
    }

    public OrderItemsResponse(final List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public static OrderItemsResponse of(final List<OrderHistory> orderHistories) {
        List<OrderItemResponse> orderItemResponses = orderHistories.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
        return new OrderItemsResponse(orderItemResponses);
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
