package cart.domain.coupon;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findAll();

    Coupon findById(Long id);

    Long add(Coupon coupon);

    void delete(Long id);
}
