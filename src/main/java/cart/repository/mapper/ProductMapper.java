package cart.repository.mapper;

import cart.dao.entity.ProductEntity;
import cart.domain.Money;
import cart.domain.Product;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductEntity toProductEntity(Product product) {
        return new ProductEntity(
            product.getId(),
            product.getName(),
            product.getImageUrl(),
            product.getPriceIntValue()
        );
    }

    public static Product toProduct(ProductEntity productEntity) {
        return new Product(
            productEntity.getId(),
            productEntity.getName(),
            Money.from(productEntity.getPrice()),
            productEntity.getImageUrl()
        );
    }

}
