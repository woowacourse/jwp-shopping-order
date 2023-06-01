package cart.dto;

import cart.domain.order.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemSelectResponse {
    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    private OrderItemSelectResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static List<OrderItemSelectResponse> from(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemSelectResponse(
                        orderItem.getId(),
                        orderItem.getQuantity(),
                        ProductResponse.from(orderItem.getProduct())))
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
