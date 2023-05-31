package cart.service;

import cart.domain.coupon.Coupon;
import cart.dto.CouponResponse;
import cart.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final MemberCouponRepository memberCouponRepository) {
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> findAllByMemberId(final Long memberId) {
        final List<Coupon> coupons = memberCouponRepository.findAllByMemberId(memberId);
        return coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }
}
