package cart.dto.order;

import cart.domain.order.Order;
import cart.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long orderId;
    private String orderDate;
    private Long totalPriceWithShippingFee;
    private List<OrderDetailsDto> orderDetailResponses;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, String orderDate, Long totalPriceWithShippingFee, List<OrderDetailsDto> orderDetailResponses) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPriceWithShippingFee = totalPriceWithShippingFee;
        this.orderDetailResponses = orderDetailResponses;
    }

    public static OrderResponse from(final Order order) {
        List<OrderDetailsDto> orderDetailsDtoList = order.getOrderItems().stream()
                .map(orderItem -> new OrderDetailsDto(orderItem.getQuantity(), ProductResponse.of(orderItem.getProduct())))
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(),
                order.getCreatedAt(),
                order.getShippingFee() + order.getTotalPrice(),
                orderDetailsDtoList
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Long getTotalPriceWithShippingFee() {
        return totalPriceWithShippingFee;
    }

    public List<OrderDetailsDto> getOrderDetailResponses() {
        return orderDetailResponses;
    }
}
