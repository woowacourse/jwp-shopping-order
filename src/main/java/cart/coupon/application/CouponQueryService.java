package cart.coupon.application;

import cart.coupon.domain.CouponRepository;
import cart.coupon.presentation.dto.AllCouponQueryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CouponQueryService {

    private final CouponRepository couponRepository;

    public CouponQueryService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public AllCouponQueryResponse findAllByMemberId(Long memberId) {
        return AllCouponQueryResponse.from(couponRepository.findAllByMemberId(memberId));
    }
}
