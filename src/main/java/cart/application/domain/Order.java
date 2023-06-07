package cart.application.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private final Long id;
    private final int price;
    private final Member member;
    private final List<CartItem> cartItems;

    private Order(final Long id, final int price, final Member member, final List<CartItem> cartItems) {
        this.id = id;
        this.price = price;
        this.member = member;
        this.cartItems = cartItems;
    }

    public static Order of(final Long id, final Member member, final List<CartItem> cartItems) {
        final int price = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
        return new Order(id, price, member, cartItems);
    }

    public static Order of(final Member member, final List<CartItem> cartItems) {
        final int price = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
        return new Order(null, price, member, cartItems);
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

    public List<Long> getCartItemIds() {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
    }

    public int getPrice() {
        return price;
    }

    public Long getMemberId() {
        return member.getId();
    }
}
