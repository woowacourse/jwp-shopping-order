package cart.application.service.coupon;

import cart.application.repository.coupon.CouponRepository;
import cart.application.service.coupon.dto.CouponResultDto;
import cart.domain.member.Member;
import cart.domain.coupon.Coupons;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CouponReadService {

    private final CouponRepository couponRepository;

    public CouponReadService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResultDto> findByMember(final Member memberAuth) {
        final Coupons coupons = couponRepository.findByMemberId(memberAuth.getId());

        return CouponResultDto.from(coupons);
    }

}
