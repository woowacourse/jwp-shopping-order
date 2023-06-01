package cart.domain.coupon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Coupons {
    private final List<Coupon> coupons;

    public Coupons(List<Coupon> coupons) {
        this.coupons = new ArrayList<>(coupons);
    }

    public boolean isExist(Coupon coupon) {
        return coupons.contains(coupon);
    }

    public List<Coupon> findCoupons(String category) {
        return coupons.stream()
                .filter(coupon -> coupon.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<Coupon> getCoupons() {
        return Collections.unmodifiableList(coupons);
    }
}
