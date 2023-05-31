package cart.domain;

import cart.domain.cartitem.CartItem;
import cart.domain.product.Product;

import java.util.Objects;

public class ProductCartItem {

    private Product product;
    private CartItem cartItem;

    public ProductCartItem(final Product product, final CartItem cartItem) {
        this.product = product;
        this.cartItem = cartItem;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductCartItem that = (ProductCartItem) o;
        return Objects.equals(product, that.product) && Objects.equals(cartItem, that.cartItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, cartItem);
    }

    public Product getProduct() {
        return product;
    }

    public CartItem getCartItem() {
        return cartItem;
    }
}
