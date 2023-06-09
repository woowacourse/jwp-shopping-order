package cart.repository;

import cart.domain.Coupon;
import cart.domain.Member;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findByMemberId(Long memberId);

    Coupon findById(Long couponId);

    Coupon save(Coupon coupon);

    void saveToMember(Member member, Coupon coupon);

    void deleteMemberCoupon(Long memberId, Long couponId);
}
