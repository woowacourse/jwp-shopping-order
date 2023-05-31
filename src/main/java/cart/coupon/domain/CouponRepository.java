package cart.coupon.domain;

import java.util.List;

public interface CouponRepository {

    Long save(Coupon coupon);

    Coupon findById(Long id);

    List<Coupon> findAllByMemberId(Long memberId);
}
