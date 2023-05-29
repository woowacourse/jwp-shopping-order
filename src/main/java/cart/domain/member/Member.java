package cart.domain.member;

import cart.domain.cart.CartItem;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private Long id;
    private MemberName name;
    private Password password;
    private List<CartItem> cartItems;

    public Member(Long id, String name, String password, List<CartItem> cartItems) {
        this.id = id;
        this.name = new MemberName(name);
        this.password = new Password(password);
        this.cartItems = cartItems;
    }

    public Member(Long id, String name, String password) {
        this(id, name, password, new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public boolean checkPassword(String password) {
        return this.password.getPassword().equals(password);
    }
}
