package cart.dto.response;

import cart.domain.order.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {

    private ProductResponse product;
    private int quantity;

    private OrderItemResponse(int quantity, ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderItemResponse of(final OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getQuantity(), ProductResponse.of(orderItem.getProduct()));
    }

    public static List<OrderItemResponse> of(final List<OrderItem> orderItems) {
        return orderItems.stream()
            .map(OrderItemResponse::of)
            .collect(Collectors.toList());
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
