package cart.repository.mapper;

import cart.dao.dto.CartItemProductDto;
import cart.dao.entity.CartItemEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.Quantity;

public class CartItemMapper {

    private CartItemMapper() {
    }

    public static CartItem toCartItem(CartItemProductDto itemProductDto) {
        return new CartItem(
            itemProductDto.getCartItemId(),
            Quantity.from(itemProductDto.getQuantity()),
            new Product(itemProductDto.getProductId(), itemProductDto.getProductName(),
                Money.from(itemProductDto.getPrice()), itemProductDto.getProductImageUrl()),
            new Member(itemProductDto.getMemberId(), itemProductDto.getEmail(), null)
        );
    }

    public static CartItemEntity toCartItemEntity(CartItem cartItem) {
        return new CartItemEntity(
            cartItem.getId(),
            cartItem.getMember().getId(),
            cartItem.getProduct().getId(),
            cartItem.getQuantityCount());
    }

}
