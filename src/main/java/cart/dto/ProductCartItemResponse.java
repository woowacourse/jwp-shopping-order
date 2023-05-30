package cart.dto;

import cart.domain.CartItem;
import cart.domain.Product;

public class ProductCartItemResponse {

    private final Product product;
    private final CartItem cartItem;

    private ProductCartItemResponse(final Product product, final CartItem cartItem) {
        this.product = product;
        this.cartItem = cartItem;
    }

    public static ProductCartItemResponse of(final Product product, final CartItem cartItem) {
        return new ProductCartItemResponse(product, cartItem);
    }

    public Product getProduct() {
        return product;
    }

    public CartItem getCartItem() {
        return cartItem;
    }
}
