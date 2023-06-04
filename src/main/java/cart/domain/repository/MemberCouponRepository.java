package cart.domain.repository;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MemberCouponRepository {
    Coupon findAvailableCouponByIdAndMemberId(Member member, Long couponId);

    List<Coupon> findAllByMemberId(Member member);
}
