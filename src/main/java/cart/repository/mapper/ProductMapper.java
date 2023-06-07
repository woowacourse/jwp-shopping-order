package cart.repository.mapper;

import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.entity.ProductEntity;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toProduct(final ProductEntity entity) {
        return new Product(
                entity.getId(),
                new Name(entity.getName()),
                new ImageUrl(entity.getImage()),
                new Price(BigDecimal.valueOf(entity.getPrice()))
        );
    }

    public static ProductEntity toEntity(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName().name(),
                product.getImage().imageUrl(),
                product.getPrice().price().longValue()
        );
    }

    public static Product toProductWithPrice(final ProductEntity entity, final Long price) {
        return new Product(
                entity.getId(),
                new Name(entity.getName()),
                new ImageUrl(entity.getImage()),
                new Price(BigDecimal.valueOf(price))
        );
    }
}
