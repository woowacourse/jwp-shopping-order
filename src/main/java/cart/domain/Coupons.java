package cart.domain;

import java.util.ArrayList;
import java.util.List;

public class Coupons {
    private final List<Coupon> productCoupons;

    public Coupons(List<Coupon> productCoupons) {
        this.productCoupons = new ArrayList<>(productCoupons);
    }

    public boolean isExist(Coupon productCoupon) {
        return productCoupons.contains(productCoupon);
    }

}
