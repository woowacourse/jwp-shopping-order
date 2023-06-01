package cart.application.service.coupon;

import cart.application.repository.CouponRepository;
import cart.domain.coupon.Coupon;
import cart.ui.MemberAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CouponReadService {

    private final CouponRepository couponRepository;

    public CouponReadService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResultDto> findByMember(final MemberAuth memberAuth) {
        final List<Coupon> coupons = couponRepository.findByMemberId(memberAuth.getId());

        return coupons.stream()
                .map(CouponResultDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

}
