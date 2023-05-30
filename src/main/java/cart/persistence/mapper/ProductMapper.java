package cart.persistence.mapper;

import cart.domain.product.Product;
import cart.domain.product.dto.ProductWithId;
import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.entity.ProductEntity;

public class ProductMapper {

    public static Product convertProduct(final ProductEntity productEntity) {
        return new Product(productEntity.getName(), productEntity.getPrice(),
            productEntity.getImageUrl());
    }

    public static Product convertProduct(final CartItemDto cartItemDto) {
        return new Product(cartItemDto.getProductName(), cartItemDto.getProductPrice(),
            cartItemDto.getProductImageUrl());
    }

    public static ProductWithId convertProductWithId(final ProductEntity productEntity) {
        return new ProductWithId(productEntity.getId(), convertProduct(productEntity));
    }

    public static ProductEntity convertProductEntity(final Product product) {
        return new ProductEntity(product.getName(), product.getImageUrl(), product.getPrice());
    }
}
