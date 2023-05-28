package cart.domain.member;

import cart.domain.coupon.Coupons;

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

    public Coupons getCoupons() {
        return coupons;
    }

    public boolean checkPassword(final String password) {
        return this.password.isPassed(password);
    }
}
