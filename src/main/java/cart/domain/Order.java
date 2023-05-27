package cart.domain;

import java.util.List;

public final class Order {
    private final Long id;
    private final Member member;
    private final int price;
    private final List<CartItem> cartItems;

    public Order(Long id, Member member, final int price, List<CartItem> cartItems) {
        this.id = id;
        this.member = member;
        this.price = price;
        this.cartItems = cartItems;
    }

    public Order(final Member member, final int price, final List<CartItem> cartItems) {
        this(null, member, price, cartItems);
    }

    public int getPrice() {
        return price;
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
