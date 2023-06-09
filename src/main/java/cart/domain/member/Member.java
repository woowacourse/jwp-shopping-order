package cart.domain.member;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Coupons coupons;

    public Member(final Long id, final String email, final String password) {
        this(id, email, password, Coupons.empty());
    }

    public Member(final Long id, final String email, final String password, final Coupons coupons) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Coupon findCoupon(final Long couponId) {
        return coupons.findCoupon(couponId);
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

    public Coupons getCoupons() {
        return coupons;
    }
}
