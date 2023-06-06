package cart.dto;

import cart.domain.CartItem;

public class CartItemResponse {

    private final Long id;
    private final ProductResponse product;
    private final int quantity;

    private CartItemResponse(final Long id, final ProductResponse product, final int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public static CartItemResponse of(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                ProductResponse.of(cartItem.getProduct()),
                cartItem.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
