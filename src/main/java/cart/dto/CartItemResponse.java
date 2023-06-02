package cart.dto;

import cart.domain.CartItem;
import java.util.List;
import java.util.stream.Collectors;

public class CartItemResponse {
    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    private CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse from(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.from(cartItem.getProduct())
        );
    }

    public static List<CartItemResponse> from(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> CartItemResponse.from(cartItem))
                .collect(Collectors.toUnmodifiableList());
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
