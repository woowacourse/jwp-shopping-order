package cart.domain.repository;

import cart.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

    Long publishUserCoupon(Member member, Long couponId);

}
