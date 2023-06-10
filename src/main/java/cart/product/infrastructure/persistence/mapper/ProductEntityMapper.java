package cart.product.infrastructure.persistence.mapper;

import cart.product.domain.Product;
import cart.product.infrastructure.persistence.entity.ProductEntity;

public class ProductEntityMapper {

    public static ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
    }

    public static Product toDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl());
    }
}
