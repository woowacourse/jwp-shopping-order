package cart.domain;

import cart.domain.order.Price;
import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        validateItemsLength(cartItems);
        this.cartItems = cartItems;
    }

    private void validateItemsLength(final List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니 상품이 존재하지 않습니다.");
        }
    }

    public Price sumOfPrice() {
        int sum = cartItems.stream()
                .mapToInt(CartItem::getPrice)
                .sum();
        return new Price(sum);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
