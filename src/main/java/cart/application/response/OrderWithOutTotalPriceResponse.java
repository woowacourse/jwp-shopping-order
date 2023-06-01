package cart.application.response;

import cart.domain.order.OrderHistory;

import java.util.List;
import java.util.stream.Collectors;

public class OrderWithOutTotalPriceResponse {

    private Long orderId;
    private List<OrderItemResponse> orderProducts;

    public OrderWithOutTotalPriceResponse(Long orderId, List<OrderItemResponse> orderProducts) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }

    public static OrderWithOutTotalPriceResponse from(OrderHistory orderHistory) {
        return new OrderWithOutTotalPriceResponse(
                orderHistory.getId(),
                collectOrderProductResponses(orderHistory)
        );
    }

    private static List<OrderItemResponse> collectOrderProductResponses(OrderHistory order) {
        return order.getOrderItems()
                .getOrderItems()
                .stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderProducts() {
        return orderProducts;
    }
}
