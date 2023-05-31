package cart.domain.member;

import cart.domain.coupon.Coupon;

import java.util.Collections;
import java.util.List;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final List<Coupon> coupons;

    private Member(Long id, String email, String password, List<Coupon> coupons) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, Collections.emptyList());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public Member setCoupons(final List<Coupon> coupons) {
        return new Member(id, email, password, coupons);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
