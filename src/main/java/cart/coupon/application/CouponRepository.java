package cart.coupon.application;

import cart.coupon.Coupon;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CouponRepository {
    private final Map<Long, Coupon> couponMap = new HashMap<>();
    private long id = 1L;

    public Long save(String name, Long discountPolicyId) {
        final var id = this.id++;
        this.couponMap.put(id, new Coupon(id, name, discountPolicyId));
        return id;
    }

    public Coupon findById(Long id) {
        return this.couponMap.get(id);
    }
}
