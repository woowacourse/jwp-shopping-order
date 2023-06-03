package cart.dto.response;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> items;
    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;

    private OrderResponse() {
    }

    private OrderResponse(Long orderId, List<OrderItemResponse> items, Long productPrice, Long discountPrice, Long deliveryFee, Long totalPrice) {
        this.orderId = orderId;
        this.items = items;
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

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getItems() {
        return items;
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
