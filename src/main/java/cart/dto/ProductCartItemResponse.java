package cart.dto;

import cart.domain.CartItem;
import cart.domain.Product;

import java.util.Optional;

public class ProductCartItemResponse {

    private final Product product;
    private final CartItemInProductCartItemDto cartItem;

    private ProductCartItemResponse(final Product product, final CartItemInProductCartItemDto cartItem) {
        this.product = product;
        this.cartItem = cartItem;
    }

    public static ProductCartItemResponse of(final Product product, final CartItem cartItem) {
        final Optional<CartItem> nullableCartItem = Optional.ofNullable(cartItem);
        if (nullableCartItem.isEmpty()) {
            return new ProductCartItemResponse(product, null);
        }

        final CartItemInProductCartItemDto cartItemDto = CartItemInProductCartItemDto.of(cartItem.getId(), cartItem.getQuantity());
        return new ProductCartItemResponse(product, cartItemDto);
    }

    public Product getProduct() {
        return product;
    }

    public CartItemInProductCartItemDto getCartItem() {
        return cartItem;
    }
}
