package cart.persistence.repository;

import cart.domain.product.Product;
import cart.persistence.entity.ProductEntity;

class Mapper {

    public static Product productEntityToProductMapper(final ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageUrl()
        );
    }

    public static ProductEntity productToProductEntityMapper(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                null
        );
    }
}
