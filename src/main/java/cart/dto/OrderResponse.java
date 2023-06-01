package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private List<OrderItemResponse> orderItemsResponses;
    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;

    private OrderResponse() {
    }

    private OrderResponse(Long id, List<OrderItemResponse> orderItemsResponses, Long productPrice, Long discountPrice, Long deliveryFee, Long totalPrice) {
        this.id = id;
        this.orderItemsResponses = orderItemsResponses;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
    }

    public static OrderResponse of(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().getOrderItems().stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), orderItemResponses, order.getProductPrice(), order.getDiscountPrice(), order.getDeliveryFee(), order.getTotalPrice());
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItemsResponses() {
        return orderItemsResponses;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}
