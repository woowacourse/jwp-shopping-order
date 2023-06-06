package cart.persistence.mapper;

import cart.domain.product.Product;
import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.ProductEntity;

public class ProductMapper {

    public static Product convertProduct(final ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(),
            productEntity.getImageUrl(), productEntity.isDeleted());
    }

    public static Product convertProduct(final CartItemDto cartItemDto) {
        return new Product(cartItemDto.getProductId(), cartItemDto.getProductName(), cartItemDto.getProductPrice(),
            cartItemDto.getProductImageUrl(), cartItemDto.isProductIsDeleted());
    }

    public static Product convertProduct(final OrderDto orderDto) {
        return new Product(orderDto.getProductId(), orderDto.getProductName(), orderDto.getProductPrice(),
            orderDto.getProductImageUrl(), orderDto.getProductIsDeleted());
    }

    public static ProductEntity convertProductEntity(final Product product) {
        return new ProductEntity(product.getName(), product.getImageUrl(), product.getPrice(), product.isDeleted());
    }
}
