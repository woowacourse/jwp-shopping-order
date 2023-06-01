package cart.application.repository;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import java.util.List;

public interface MemberCouponRepository {

    List<MemberCoupon> findValidCouponsByMember(Member member);
}
