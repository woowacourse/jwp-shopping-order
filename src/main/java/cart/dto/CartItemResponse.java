package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.CartItem;

public class CartItemResponse {

    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse() {
    }

    public CartItemResponse(Long id, int quantity, ProductResponse product) {
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

    public static List<CartItemResponse> of(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
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
