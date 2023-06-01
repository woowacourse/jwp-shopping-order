package cart.mapper;

import cart.domain.Product;
import cart.dto.ProductResponse;
import cart.entity.ProductEntity;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static Product toProduct(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
    }
}
