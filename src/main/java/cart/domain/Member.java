package cart.domain;

import java.util.List;

public class Member {

    private Long id;
    private String name;
    private Cart cart;
    private List<MemberCoupon> coupons;
    private List<Order> orders;
    private String password;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.password = password;
    }

    public Member(final Long id, final String name, final Cart cart, final List<MemberCoupon> coupons, final List<Order> orders, final String password) {
        this.id = id;
        this.name = name;
        this.cart = cart;
        this.coupons = coupons;
        this.orders = orders;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Cart getCart() {
        return cart;
    }

    public List<MemberCoupon> getCoupons() {
        return coupons;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String getEmail() {
        return "email";
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
