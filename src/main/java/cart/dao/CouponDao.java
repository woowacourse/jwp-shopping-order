package cart.dao;

import cart.domain.Coupon;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    public Optional<Coupon> findById(final long id, final Long memberId) {
        return null;
    }

    public void update(final Coupon usedCoupon) {

    }
}
