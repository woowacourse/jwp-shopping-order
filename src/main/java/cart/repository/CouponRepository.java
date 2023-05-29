package cart.repository;

import cart.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

    Long issue(final Member member, final Long couponId);

    void changeStatus(Long couponId, Long memberId);
}
