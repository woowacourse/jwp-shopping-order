package cart.ui.cartitem.dto;

import cart.application.service.cartitem.dto.CartResultDto;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {

    private List<CartItemResponse> cartItems;
    private int totalPrice;

    public CartResponse() {
    }

    private CartResponse(List<CartItemResponse> cartItems, int totalPrice) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }

    public static CartResponse from(CartResultDto cartResult) {
        List<CartItemResponse> cartItemResponses = cartResult.getCartItemResultDtos().stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toUnmodifiableList());

        return new CartResponse(cartItemResponses, cartResult.getTotalPrice());
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

}
