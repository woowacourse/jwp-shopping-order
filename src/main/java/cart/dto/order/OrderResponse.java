package cart.dto.order;

import cart.domain.order.Order;
import cart.dto.product.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long orderId;
    private String orderDate;
    private Long totalProductsPrice;
    private Long shippingFee;
    private Long usedPoint;
    private List<OrderDetailsDto> orderDetails;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, String orderDate, Long totalProductsPrice, Long shippingFee, Long usedPoint, List<OrderDetailsDto> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalProductsPrice = totalProductsPrice;
        this.shippingFee = shippingFee;
        this.usedPoint = usedPoint;
        this.orderDetails = orderDetails;
    }

    public static OrderResponse from(final Order order) {
        List<OrderDetailsDto> orderDetailsDtoList = order.getOrderItems().stream()
                .map(orderItem -> new OrderDetailsDto(orderItem.getQuantity(), ProductResponse.of(orderItem.getProduct())))
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(),
                order.getCreatedAt(),
                order.getTotalPrice(),
                order.getShippingFee(),
                order.getUsedPoint(),
                orderDetailsDtoList
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Long getTotalProductsPrice() {
        return totalProductsPrice;
    }

    public Long getShippingFee() {
        return shippingFee;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public List<OrderDetailsDto> getOrderDetails() {
        return orderDetails;
    }
}
