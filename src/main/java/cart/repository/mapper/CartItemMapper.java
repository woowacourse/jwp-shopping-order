package cart.repository.mapper;

import cart.dao.entity.CartItemEntity;
import cart.domain.cartitem.CartItem;

public class CartItemMapper {

    public static CartItem toDomain(CartItemEntity cartItemEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                MemberMapper.toDomain(cartItemEntity.getMemberEntity()),
                ProductMapper.toDomain(cartItemEntity.getProductEntity())
        );
    }

    public static CartItemEntity toEntity(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                MemberMapper.toEntity(cartItem.getMember()),
                ProductMapper.toEntity(cartItem.getProduct()),
                cartItem.getQuantity()
        );
    }
}
