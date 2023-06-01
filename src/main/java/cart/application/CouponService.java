package cart.application;

import cart.domain.Member;
import cart.domain.repository.CouponRepository;
import cart.dto.request.CouponCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Long publishUserCoupon(Member member, CouponCreateRequest request) {
        return couponRepository.publishUserCoupon(member, request.getId());
    }
}
