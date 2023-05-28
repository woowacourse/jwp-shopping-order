package cart.domain.cart;

import cart.domain.product.Product;
import cart.exception.CartItemNotFoundException;

import java.util.List;
import java.util.Optional;

public class CartItems {

    private static final int EMPTY_COUNT = 0;

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

    public void changeQuantity(final long cartItemId, final int quantity) {
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.hasSameId(cartItemId))
                .findAny()
                .orElseThrow(CartItemNotFoundException::new);

        if (quantity == EMPTY_COUNT) {
            this.remove(cartItem);
            return;
        }

        cartItem.changeQuantity(quantity);
    }

    public boolean hasItem(final CartItem cartItem) {
        return cartItems.stream()
                .anyMatch(item -> item.hasSameId(cartItem.getId()));
    }

    public int getTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getPrice)
                .sum();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
