package cart.cartitem.infrastructure.persistence.mapper;

import cart.cartitem.domain.CartItem;
import cart.cartitem.infrastructure.persistence.entity.CartItemEntity;

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
                entity.getProductId(),
                entity.getName(),
                entity.getImageUrl(),
                entity.getProductPrice(),
                entity.getMemberId()
        );
    }
}
