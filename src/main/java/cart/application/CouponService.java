package cart.application;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.dto.CouponRequest;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void issueCoupon(final Member member, final CouponRequest couponRequest) {
        couponRepository.issueCoupon(member.getId(), couponRequest.getId());
    }

    public List<CouponResponse> showAllCoupons(final Member member) {
        return couponRepository.findAllCoupons(member.getId()).entrySet().stream()
                .map(entry -> CouponResponse.issuableOf(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<CouponResponse> showMembersCoupons(Member member) {
        return couponRepository.findCouponsByMemberId(member.getId()).stream()
                .map(coupon -> CouponResponse.of((Optional<Coupon>) Optional.of(coupon)))
                .collect(Collectors.toList());
    }
}
