package cart.domain.coupon;

import java.util.List;

public class Coupons {

    private final List<Coupon> coupons;

    public Coupons(final List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
