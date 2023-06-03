package cart.application.repository;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import java.util.List;
import java.util.Optional;

public interface MemberCouponRepository {

    long create(MemberCoupon memberCoupon);
    
    List<MemberCoupon> findValidCouponsByMember(Member member);

    Optional<MemberCoupon> findById(long id);

    void use(MemberCoupon memberCoupon);
}
