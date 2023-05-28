package cart.domain.member;

import cart.domain.cart.Cart;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;

import java.util.List;
import java.util.Objects;

public class Member {

    private Long id;
    private final Email email;
    private final Password password;
    private Cart cart;
    private Coupons coupons;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        cart = null;
        this.coupons = null;
    }

    public void initCoupons(final Coupons coupons) {
        this.coupons = coupons;
    }

    public void initCart(final Cart cart) {
        this.cart = cart;
    }

    public boolean checkPassword(final String password) {
        return this.password.isPassed(password);
    }

    public boolean hasCoupons() {
        return this.coupons != null;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public Cart getCart() {
        return cart;
    }

    public List<Coupon> getCoupons() {
        return coupons.getCoupons();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
