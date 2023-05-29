package cart.application.mapper;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.persistence.entity.ProductEntity;

public class ProductMapper {

    public static ProductEntity toEntity(final Product product) {
        return new ProductEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static Product toProduct(final ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getImageUrl());
    }

    public static Product toProduct(final ProductRequest request) {
        return new Product(request.getName(), request.getPrice(), request.getImageUrl());
    }

    public static Product toProduct(final long productId, final ProductRequest request) {
        return new Product(productId, request.getName(), request.getPrice(), request.getImageUrl());
    }
}
