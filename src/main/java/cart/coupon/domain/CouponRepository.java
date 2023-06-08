package cart.coupon.domain;

import java.util.List;

public interface CouponRepository {

    Long save(Coupon coupon);

    Coupon findById(Long id);

    List<Coupon> findAllByMemberId(Long memberId);

    List<Coupon> findAllByIds(List<Long> ids);
}
