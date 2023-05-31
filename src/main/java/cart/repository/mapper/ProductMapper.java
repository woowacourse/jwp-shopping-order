package cart.repository.mapper;

import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toProduct(final ProductEntity entity) {
        return new Product(
                entity.getId(),
                new Name(entity.getName()),
                new ImageUrl(entity.getImage()),
                new Price(entity.getPrice())
        );
    }

    public static ProductEntity toEntity(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName().name(),
                product.getImage().imageUrl(),
                product.getPrice().price()
        );
    }
}
