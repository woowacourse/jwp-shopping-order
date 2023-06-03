package cart.cartitem.ui.response;

import cart.cartitem.domain.CartItem;
import cart.product.ui.response.ProductResponse;

public class CartItemResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.of(cartItem.getProduct())
        );
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
