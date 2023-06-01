package cart.domain.repository;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberCouponRepository {
    Coupon findAvailableCouponByMember(Member member, Long couponId);

    void changeUserUsedCouponAvailability(Coupon coupon);

    List<Coupon> findMemberCoupons(Member member);
}
