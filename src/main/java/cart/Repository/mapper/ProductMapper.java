package cart.Repository.mapper;

import cart.domain.Product.Product;
import cart.entity.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class ProductMapper {

    public static List<Product> toProducts(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(ProductMapper::toProduct)
                .collect(Collectors.toUnmodifiableList());
    }

    public static Product toProduct(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImage()
        );
    }

    public static ProductEntity toProductEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName().name(),
                product.getPrice().price(),
                product.getImageUrl().imageUrl()
        );
    }

    public static Map<Long, Product> productMappingById(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(ProductMapper::toProduct)
                .collect(toMap(Product::getId, product -> product));
    }
}
