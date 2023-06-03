package cart.domain.repository;

import cart.domain.Coupon;
import java.util.List;

public interface CouponRepository {

    List<Coupon> findAll();
}
