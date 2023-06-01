package cart.persistence.mapper;

import cart.domain.product.Product;
import cart.domain.product.dto.ProductWithId;
import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.ProductEntity;

public class ProductMapper {

    public static Product convertProduct(final ProductEntity productEntity) {
        return new Product(productEntity.getName(), productEntity.getPrice(),
            productEntity.getImageUrl(), productEntity.isDeleted());
    }

    public static Product convertProduct(final CartItemDto cartItemDto) {
        return new Product(cartItemDto.getProductName(), cartItemDto.getProductPrice(),
            cartItemDto.getProductImageUrl(), cartItemDto.isProductIsDeleted());
    }

    public static Product convertProduct(final OrderDto orderDto) {
        return new Product(orderDto.getProductName(), orderDto.getProductPrice(),
            orderDto.getProductImageUrl(), orderDto.getProductIsDeleted());
    }

    public static ProductWithId convertProductWithId(final CartItemDto cartItemDto) {
        return new ProductWithId(cartItemDto.getProductId(), convertProduct(cartItemDto));
    }

    public static ProductWithId convertProductWithId(final ProductEntity productEntity) {
        return new ProductWithId(productEntity.getId(), convertProduct(productEntity));
    }

    public static ProductWithId convertProductWithId(final OrderDto orderDto) {
        final Product product = convertProduct(orderDto);
        return new ProductWithId(orderDto.getProductId(), product);
    }

    public static ProductEntity convertProductEntity(final Product product) {
        return new ProductEntity(product.getName(), product.getImageUrl(), product.getPrice(), product.isDeleted());
    }
}
