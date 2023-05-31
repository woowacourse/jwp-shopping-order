package cart.dto;

import cart.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int stock;

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getStock());
    }
}
