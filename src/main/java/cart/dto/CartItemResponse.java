package cart.dto;

import cart.domain.CartItem;
import java.util.List;
import java.util.stream.Collectors;

public class CartItemResponse {
    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    private CartItemResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    private static CartItemResponse from(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.of(cartItem.getProduct())
        );
    }

    public static List<CartItemResponse> from(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItemResponse::from)
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
