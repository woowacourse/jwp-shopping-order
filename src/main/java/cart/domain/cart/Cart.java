package cart.domain.cart;

import cart.domain.member.Member;
import cart.domain.product.Price;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {

    private final List<CartItem> cartItems;

    public Cart(final List<CartItem> cartItems) {
        this.cartItems = new ArrayList<>(cartItems);
    }

    public void checkOwner(final Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public Price getTotalPrice() {
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(Price.minPrice(), Price::add);
    }

    public List<CartItem> getCartItems() {
        return Collections.unmodifiableList(cartItems);
    }
}
