package cart.domain.cart;

import cart.domain.product.Product;

import java.util.List;
import java.util.Optional;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public CartItem add(final Product product) {
        Optional<CartItem> cartItem = cartItems.stream()
                .filter(item -> item.hasProduct(product))
                .findAny();

        if (cartItem.isPresent()) {
            CartItem insertItem = cartItem.orElseThrow(IllegalArgumentException::new);
            insertItem.addQuantity();
            return insertItem;
        }

        CartItem insertItem = new CartItem(product);
        cartItems.add(insertItem);

        return insertItem;
    }

    public void remove(final CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void changeQuantity(final long cartItemId, final int quantity) {
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.hasSameId(cartItemId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("cartItem을 찾을 수 없습니다."));

        if (quantity == 0) {
            this.remove(cartItem);
            return;
        }

        cartItem.changeQuantity(quantity);
    }
}
