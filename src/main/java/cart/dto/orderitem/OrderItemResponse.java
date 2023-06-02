package cart.dto.orderitem;

import cart.domain.OrderItem;
import cart.dto.product.ProductResponse;

public class OrderItemResponse {
    private final Long id;
    private final Integer quantity;
    private final ProductResponse productResponse;

    private OrderItemResponse(final Long id, final Integer quantity, final ProductResponse productResponse) {
        this.id = id;
        this.quantity = quantity;
        this.productResponse = productResponse;
    }

    public static OrderItemResponse from(final OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getQuantity(),
                ProductResponse.from(orderItem.getProduct()));
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }
}
