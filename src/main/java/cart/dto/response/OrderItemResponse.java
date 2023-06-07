package cart.dto.response;

import cart.domain.OrderItemEntity;
import cart.domain.Product;

public class OrderItemResponse {

    private final int quantity;
    private final ProductResponse product;

    public OrderItemResponse(int quantity, ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderItemResponse of(final OrderItemEntity orderItemEntity, final Product product) {
        return new OrderItemResponse(
                orderItemEntity.getQuantity(),
                ProductResponse.of(orderItemEntity, product)
        );
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
