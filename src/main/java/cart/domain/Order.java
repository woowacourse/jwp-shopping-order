package cart.domain;

import java.util.List;

public class Order {
    private Long id;
    private Member member;
    private List<CartItem> cartItems;


    public Order(Member member, List<CartItem> cartItems) {
        this.member = member;
        this.cartItems = cartItems;
    }

    public Order(Long id, Member member, List<CartItem> cartItems) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
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
