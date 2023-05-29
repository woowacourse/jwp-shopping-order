package cart.service;

import cart.domain.coupon.Coupon;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> findAllByMemberId(final Long memberId) {
        final List<Coupon> coupons = couponRepository.findAllByMemberId(memberId);
        return coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

}
