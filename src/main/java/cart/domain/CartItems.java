package cart.domain;

import cart.exception.OrderException;

import java.util.ArrayList;
import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = new ArrayList<>(cartItems);
    }

    public void validateOwner(final Member member) {
        for (final CartItem cartItem : cartItems) {
            cartItem.validateOwner(member);
        }
    }

    public void validateTotalProductPrice(final int expectPrice) {
        int calculatedPrice = cartItems.stream()
                .map(CartItem::calculateTotalProductPrice)
                .mapToInt(Price::getValue)
                .sum();

        if (expectPrice != calculatedPrice) {
            throw new OrderException.MismatchedTotalProductPrice("요청의 상품 가격과 실제 상품 가격이 다릅니다.");
        }
    }

    public List<CartItem> getCartItems() {
        return List.copyOf(cartItems);
    }
}
