package cart.domain.cart;

import cart.domain.member.Member;
import cart.domain.product.Product;

import java.util.List;

public class Cart {

    private Long id;
    private final Member member;
    private final CartItems cartItems;

    public Cart(final Long id, final Member member, final CartItems cartItems) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
    }

    public Cart(final Member member, final CartItems cartItems) {
        this.member = member;
        this.cartItems = cartItems;
    }

    public CartItem addItem(final Product product) {
        return cartItems.add(product);
    }

    public void removeItem(final CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void changeQuantity(final long cartItemId, final int quantity) {
        cartItems.changeQuantity(cartItemId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getCartItems() {
        return cartItems.getCartItems();
    }
}
