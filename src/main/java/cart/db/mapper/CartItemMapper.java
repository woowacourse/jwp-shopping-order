package cart.db.mapper;

import cart.db.entity.CartItemDetailEntity;
import cart.db.entity.CartItemEntity;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemMapper {

    public static CartItem toDomain(final CartItemDetailEntity cartItemDetailEntity) {
        return new CartItem(
                cartItemDetailEntity.getId(),
                cartItemDetailEntity.getQuantity(),
                new Product(
                        cartItemDetailEntity.getProductId(),
                        cartItemDetailEntity.getProductName(),
                        cartItemDetailEntity.getProductPrice(),
                        cartItemDetailEntity.getProductImageUrl()
                ),
                new Member(
                        cartItemDetailEntity.getMemberId(),
                        cartItemDetailEntity.getMemberName(),
                        cartItemDetailEntity.getMemberPassword()
                )
        );
    }

    public static List<CartItem> toDomain(final List<CartItemDetailEntity> cartItemDetailEntities) {
        return cartItemDetailEntities.stream()
                .map(CartItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static CartItemEntity toEntity(final CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getItem().getProduct().getId(),
                cartItem.getItem().getQuantity()
        );
    }

//    public static CartItemDetailEntity toEntity(final CartItem cartItem) {
//        return new CartItemDetailEntity(
//                cartItem.getId(),
//                cartItem.getMember().getId(),
//                car
//                new Product(
//                        cartItem.getProductId(),
//                        cartItem.getProductName(),
//                        cartItem.getProductPrice(),
//                        cartItem.getProductImageUrl()
//                ),
//                new Member(
//                        cartItem.getMemberId(),
//                        cartItem.getMemberName(),
//                        cartItem.getMemberPassword()
//                )
//        );
//    }

}
