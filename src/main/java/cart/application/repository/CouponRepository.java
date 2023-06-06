package cart.application.repository;

import cart.application.service.coupon.dto.MemberCouponDto;
import cart.domain.coupon.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findUsableCouponByMemberCouponId(Long memberCouponId);

    List<MemberCouponDto> findByMemberId(Long memberId);

    void convertToUseMemberCoupon(Long memberCouponId);

    long createOrderedCoupon(Long orderId, final Long memberCouponId);

    List<Coupon> findUsedCouponByOrderId(Long orderId);
}
