package cart.dto.response;

import cart.domain.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {
    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    public OrderItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static List<OrderItemResponse> of(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemResponse(
                        orderItem.getId(),
                        orderItem.getQuantity(),
                        ProductResponse.of(orderItem.getProduct())
                ))
                .collect(Collectors.toList());
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
