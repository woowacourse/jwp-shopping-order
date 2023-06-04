package cart.domain.coupon.repository;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.IssuableCoupon;
import java.util.List;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    IssuableCoupon saveIssuableCoupon(IssuableCoupon issuableCoupon);

    List<IssuableCoupon> findAllIssuable();
}
