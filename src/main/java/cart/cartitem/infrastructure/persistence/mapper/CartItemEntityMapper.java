package cart.cartitem.infrastructure.persistence.mapper;

import cart.cartitem.domain.CartItem;
import cart.cartitem.infrastructure.persistence.entity.CartItemEntity;
import cart.product.domain.Product;

public class CartItemEntityMapper {

    public static CartItemEntity toEntity(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getProductId(),
                cartItem.getName(),
                cartItem.getImageUrl(),
                cartItem.getPrice(),
                cartItem.getMemberId()
        );
    }

    public static CartItem toDomain(CartItemEntity entity) {
        return new CartItem(
                entity.getId(),
                entity.getQuantity(),
                new Product(entity.getProductId(), entity.getName(), entity.getProductPrice(), entity.getImageUrl()),
                entity.getMemberId()
        );
    }
}
