package cart.application;

import cart.domain.Member;
import cart.dto.CouponIssueRequest;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Long issueCoupon(final Member member, final CouponIssueRequest request) {
        return couponRepository.issue(member, request.getId());
    }
}
