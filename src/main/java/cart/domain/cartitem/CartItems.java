package cart.domain.cartitem;

import cart.domain.member.Member;
import java.util.List;
import java.util.stream.Collectors;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void checkOwner(Member member) {
        for (CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
    }

    public int calculateTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    public List<Long> extractAllIds() {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
