package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.OrderItem;

public class OrderItemResponse {

    private final Long orderItemId;
    private final ProductResponse product;
    private final Integer total;
    private final Integer quantity;

    public OrderItemResponse(Long orderItemId, ProductResponse product, Integer total, Integer quantity) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.total = total;
        this.quantity = quantity;
    }

    public static OrderItemResponse of(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                ProductResponse.of(orderItem.getOrderedProduct()),
                orderItem.getTotal().getValue(),
                orderItem.getQuantity()
        );
    }

    public static List<OrderItemResponse> of(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
