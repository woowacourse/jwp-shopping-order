package cart.domain;

import cart.domain.coupon.Coupon;
import java.util.List;
import java.util.Objects;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final List<Coupon> coupons;

    public Member(final Long id, final String email, final String password, final List<Coupon> coupons) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
