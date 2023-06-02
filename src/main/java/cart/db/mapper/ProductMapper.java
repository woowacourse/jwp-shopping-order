package cart.db.mapper;

import cart.db.entity.ProductEntity;
import cart.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toDomain(final ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl()
        );
    }

    public static List<Product> toDomain(final List<ProductEntity> memberEntities) {
        return memberEntities.stream()
                .map(ProductMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static ProductEntity toEntity(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
