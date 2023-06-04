package cart.dto;

import cart.domain.OrderItemEntity;
import cart.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductResponse {

    private final Long productId;
    private final int price;
    private final String name;
    private final String imageUrl;
    private final int stock;

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getPrice(), product.getName(), product.getImageUrl(), product.getStock());
    }

    public static ProductResponse of(final OrderItemEntity orderItemEntity, final Product product) {
        return new ProductResponse(
                orderItemEntity.getProductId(),
                orderItemEntity.getProductPrice(),
                orderItemEntity.getProductName(),
                orderItemEntity.getProductImageUrl(),
                product.getStock()
        );
    }
}
