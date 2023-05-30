package cart.dto.order;

import cart.domain.order.Order;
import cart.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {
    private Long orderId;
    private String orderDate;
    private List<OrderDetailsDto> orderDetailResponses;

    public OrdersResponse() {
    }

    public OrdersResponse(Long orderId, String orderDate, List<OrderDetailsDto> orderDetailResponses) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderDetailResponses = orderDetailResponses;
    }

    public static OrdersResponse from(Order order) {
        List<OrderDetailsDto> orderDetailsDtoList = order.getOrderItems().stream()
                .map(orderItem -> new OrderDetailsDto(orderItem.getQuantity(), ProductResponse.of(orderItem.getProduct())))
                .collect(Collectors.toList());
        return new OrdersResponse(order.getId(), order.getCreatedAt(), orderDetailsDtoList);
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public List<OrderDetailsDto> getOrderDetails() {
        return orderDetailResponses;
    }
}
