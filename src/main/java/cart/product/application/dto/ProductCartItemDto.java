package cart.product.application.dto;

import cart.cartitem.domain.CartItem;
import cart.product.domain.Product;

import java.util.Objects;

public class ProductCartItemDto {

    private Product product;
    private CartItem cartItem;

    public ProductCartItemDto(final Product product, final CartItem cartItem) {
        this.product = product;
        this.cartItem = cartItem;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductCartItemDto that = (ProductCartItemDto) o;
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
