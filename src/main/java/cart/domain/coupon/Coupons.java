package cart.domain.coupon;

import java.util.ArrayList;
import java.util.List;

public class Coupons {
    private final List<Coupon> coupons;

    public Coupons(List<Coupon> coupons) {
        this.coupons = new ArrayList<>(coupons);
    }

    public boolean isExist(Coupon coupon) {
        return coupons.contains(coupon);
    }

}
