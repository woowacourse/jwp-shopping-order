package cart.application.service.cartitem.dto;

import cart.domain.cartitem.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartResultDto {

    private final List<CartItemResultDto> cartItemResultDtos;
    private final int totalPrice;

    private CartResultDto(List<CartItemResultDto> cartItemResultDtos, int totalPrice) {
        this.cartItemResultDtos = cartItemResultDtos;
        this.totalPrice = totalPrice;
    }

    public static CartResultDto of(List<CartItem> cartItems, int totalPrice) {
        List<CartItemResultDto> cartItemResults = cartItems.stream()
                .map(CartItemResultDto::of)
                .collect(Collectors.toUnmodifiableList());

        return new CartResultDto(cartItemResults, totalPrice);
    }

    public List<CartItemResultDto> getCartItemResultDtos() {
        return cartItemResultDtos;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

}
