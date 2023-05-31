package cart.dto.cartItem;

import cart.domain.cartItem.CartItem;
import cart.dto.product.ProductResponse;

public class CartItemResponse {

    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse() {
    }

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
