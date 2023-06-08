package cart.domain.coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coupons {
    private final List<Coupon> coupons;

    public Coupons(List<Coupon> coupons) {
        this.coupons = new ArrayList<>(coupons);
    }

    public List<Coupon> findCoupons(Category category) {
        return coupons.stream()
                .filter(coupon -> coupon.isSupport(category))
                .collect(Collectors.toList());
    }

}
