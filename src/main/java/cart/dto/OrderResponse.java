package cart.dto;

import cart.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderItemResponse> products;
    private final String orderStatus;

    private OrderResponse(final Long orderId,
                          final List<OrderItemResponse> products,
                          final String orderStatus) {
        this.orderId = orderId;
        this.products = products;
        this.orderStatus = orderStatus;
    }

    public static List<OrderResponse> from(final List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        OrderItemResponse.from(order.getOrderItems()),
                        order.getStatus().getValue()))
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
