package cart.dto;

import cart.domain.OrderItemEntity;
import cart.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderItemResponse {

    private final int quantity;
    private final ProductResponse product;

    public static OrderItemResponse of(final OrderItemEntity orderItemEntity, final Product product) {
        return new OrderItemResponse(
                orderItemEntity.getQuantity(),
                ProductResponse.of(orderItemEntity, product)
        );
    }
}
