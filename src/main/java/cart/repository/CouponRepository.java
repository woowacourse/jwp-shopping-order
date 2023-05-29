package cart.repository;

import cart.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

    public Long issue(final Member member, final Long couponId);
}
