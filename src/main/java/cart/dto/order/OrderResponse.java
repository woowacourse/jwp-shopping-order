package cart.dto.order;

import cart.domain.Order;
import cart.dto.orderitem.OrderItemResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final Integer originalPrice;
    private final Integer discountPrice;
    private final Integer finalPrice;
    private final LocalDateTime createdAt;
    private final List<OrderItemResponse> orderProducts;

    private OrderResponse(final Long id, final Integer originalPrice, final Integer discountPrice,
                         final Integer finalPrice, final LocalDateTime createdAt,
                         final List<OrderItemResponse> orderProducts) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
        this.createdAt = createdAt;
        this.orderProducts = orderProducts;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(order.getId(),
                order.getPayment().getOriginalPrice().getValue(),
                order.getPayment().getDiscountPrice().getValue(),
                order.getPayment().getFinalPrice().getValue(),
                order.getCreatedAt(),
                createOrderItemResponses(order));
    }

    private static List<OrderItemResponse> createOrderItemResponses(final Order order) {
        return order.getOrderItems().getItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getOrderProducts() {
        return orderProducts;
    }
}
