package cart.product.application.dto;

import cart.cartitem.domain.CartItem;
import cart.product.domain.Product;

public class ProductCartItemDto {

    private Product product;
    private CartItem cartItem;

    public ProductCartItemDto(final Product product, final CartItem cartItem) {
        this.product = product;
        this.cartItem = cartItem;
    }

    public Product getProduct() {
        return product;
    }

    public CartItem getCartItem() {
        return cartItem;
    }
}
