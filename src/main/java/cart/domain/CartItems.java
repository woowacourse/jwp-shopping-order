package cart.domain;

import java.util.List;

public class CartItems {
    private final List<CartItem> cartItems;

    public CartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void checkOwner(Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }
    
    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
