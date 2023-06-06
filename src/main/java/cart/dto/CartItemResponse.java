package cart.dto;

import cart.domain.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.of(cartItem.getProduct())
        );
    }

    public static List<CartItemResponse> createCartItemResponses(List<CartItem> cartItems) {
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
