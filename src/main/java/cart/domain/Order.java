package cart.domain;

import java.util.List;

public final class Order {
    private final Long id;
    private final Member member;
    private final List<CartItem> cartItems;

    public Order(Long id, Member member, List<CartItem> cartItems) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
    }
}
