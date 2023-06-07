package cart.domain;

import java.util.List;

public final class Order {
    private final Long id;
    private final Member member;
    private final int totalPrice;
    private final List<CartItem> cartItems;

    public Order(Long id, Member member, final int totalPrice, List<CartItem> cartItems) {
        this.id = id;
        this.member = member;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public Order(final Member member, final int totalPrice, final List<CartItem> cartItems) {
        this(null, member, totalPrice, cartItems);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
