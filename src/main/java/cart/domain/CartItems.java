package cart.domain;

import java.util.List;

import cart.exception.InvalidOrderException;

public class CartItems {
    private final List<CartItem> cartItems;

    public CartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer calculatePriceSum() {
        return cartItems.stream()
                .mapToInt(CartItem::calculateTotalPrice)
                .sum();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void validateAllCartItemsBelongsToMember(Member member) {
        final boolean isNotBelongs = cartItems.stream()
                .anyMatch(cartItem -> cartItem.isNotBelongsToMember(member));
        if (isNotBelongs) {
            throw new InvalidOrderException("Some of cart items doesn't belong to member.");
        }
    }
}
