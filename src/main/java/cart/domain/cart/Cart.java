package cart.domain.cart;

import cart.domain.member.Member;

import java.util.List;

public class Cart {

    private final Member member;
    private final List<CartItem> items;

    public Cart(final Member member, final List<CartItem> items) {
        this.member = member;
        this.items = items;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getItems() {
        return items;
    }
}
