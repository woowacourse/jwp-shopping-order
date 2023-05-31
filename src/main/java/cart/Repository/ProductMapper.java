package cart.Repository;

import cart.domain.Product.Product;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public List<Product> toProducts(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(this::toProduct)
                .collect(Collectors.toUnmodifiableList());
    }

    public Product toProduct(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImage()
        );
    }

    public ProductEntity toProductEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName().name(),
                product.getPrice().price(),
                product.getImageUrl().imageUrl()
        );
    }
}
