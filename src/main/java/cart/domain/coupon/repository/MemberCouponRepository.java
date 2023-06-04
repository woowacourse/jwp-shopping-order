package cart.domain.coupon.repository;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import java.util.List;
import java.util.Optional;

public interface MemberCouponRepository {

    Optional<MemberCoupon> findById(Long id);

    MemberCoupon save(MemberCoupon memberCoupon);

    void delete(MemberCoupon memberCoupon);

    List<MemberCoupon> findNotExpired(Member member);

    void saveAll(List<MemberCoupon> memberCoupons);
}
