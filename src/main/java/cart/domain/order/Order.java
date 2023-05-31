package cart.domain.order;

import cart.domain.cart.Cart;
import cart.domain.member.Member;

public class Order {

    private final Long id;
    private final Member member;
    private final Cart cart;

    public Order(final Long id, final Member member, final Cart cart) {
        this.id = id;
        this.member = member;
        this.cart = cart;
    }


}
