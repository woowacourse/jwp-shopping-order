package cart.domain.member;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;

import java.util.List;
import java.util.Objects;

public class Member {

    private Long id;
    private final Email email;
    private final Password password;
    private Coupons coupons;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.coupons = null;
    }

    public void initCoupons(final Coupons coupons) {
        this.coupons = coupons;
    }

    public boolean checkPassword(final String password) {
        return this.password.isPassed(password);
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
