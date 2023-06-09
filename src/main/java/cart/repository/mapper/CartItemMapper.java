package cart.repository.mapper;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.Quantity;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartItemMapper {

    public static Cart toCart(
            final List<CartItemEntity> cartItemEntity,
            final Map<Long, ProductEntity> productsInCart,
            final MemberEntity memberEntity
    ) {
        return new Cart(cartItemEntity.stream().map(it ->
                CartItemMapper.toCartItem(
                        it,
                        productsInCart.get(it.getProductId()),
                        memberEntity
                )
        ).collect(Collectors.toUnmodifiableList()));
    }

    public static CartItem toCartItem(
            final CartItemEntity cartItemEntity,
            final ProductEntity productEntity,
            final MemberEntity memberEntity
    ) {
        return new CartItem(
                cartItemEntity.getId(),
                new Quantity(cartItemEntity.getQuantity()),
                ProductMapper.toProduct(productEntity),
                MemberMapper.toMember(memberEntity)
        );
    }

    public static CartItemEntity toEntity(final CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity().quantity()
        );
    }
}
