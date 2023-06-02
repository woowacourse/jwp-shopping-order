package cart.domain;

import cart.exception.InvalidOrderCheckedException;

import java.util.ArrayList;
import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(List<CartItem> cartItems) {
        this.cartItems = new ArrayList<>(cartItems);
    }

    public void validateAllCartItemsOrderedLegally(CartItems serverItems) {
        if (cartItems.size() != serverItems.size() || cartItems.stream().anyMatch(cartItem -> !cartItem.isChecked())) {
            throw new InvalidOrderCheckedException();
        }

        validateContainingSameItemsWith(serverItems);
    }

    public int size() {
        return cartItems.size();
    }

    private void validateContainingSameItemsWith(CartItems other) {
        cartItems.forEach(cartItem -> validateCartItemIsContained(cartItem, other));
    }

    private void validateCartItemIsContained(final CartItem cartItem, final CartItems cartItems) {
        CartItem otherCartItem = cartItems.findById(cartItem.getId());
        cartItem.validateSameItems(otherCartItem);
    }

    private CartItem findById(Long id) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.isSameId(id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 cartItem이 없습니다."));
    }
}
