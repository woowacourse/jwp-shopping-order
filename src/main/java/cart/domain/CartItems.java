package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class CartItems {
    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Price getTotalPrice() {
        int totalPrice = cartItems.stream()
                .mapToInt(cartItem -> {
                    Product product = cartItem.getProduct();
                    return product.getPrice() * cartItem.getQuantity();
                })
                .sum();

        return new Price(totalPrice);
    }

    public CartItems getContainedCartItems(List<Long> cartItems) {
        List<CartItem> containedCartItems = this.cartItems.stream()
                .filter(cartItem -> cartItems.contains(cartItem.getId()))
                .collect(Collectors.toList());

        return new CartItems(containedCartItems);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
