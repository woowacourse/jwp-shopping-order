package cart.mapper;

import cart.domain.Product;
import cart.dto.response.ProductResponse;
import cart.entity.ProductEntity;
import cart.exception.ProductException;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        try {
            return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
        } catch (Exception e) {
            throw new ProductException.ParseFail(null);
        }
    }

    public static Product toProduct(ProductEntity productEntity) {
        try {
            return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
        } catch (Exception e) {
            throw new ProductException.ParseFail(null);
        }
    }
}
