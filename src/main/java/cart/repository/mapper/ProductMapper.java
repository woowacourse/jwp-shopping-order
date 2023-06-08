package cart.repository.mapper;

import cart.dao.dto.product.ProductDto;
import cart.domain.Money;
import cart.domain.Product;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDto toProductDto(Product product) {
        return new ProductDto(
            product.getId(),
            product.getName(),
            product.getImageUrl(),
            product.getPriceIntValue()
        );
    }

    public static Product toProduct(ProductDto productDto) {
        return new Product(
            productDto.getId(),
            productDto.getName(),
            Money.from(productDto.getPrice()),
            productDto.getImageUrl()
        );
    }

}
