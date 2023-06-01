package cart.service.coupon;

import cart.controller.dto.CouponResponse;
import cart.controller.dto.CouponTypeResponse;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
public class CouponProvider {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public CouponProvider(final CouponRepository couponRepository, final CouponMapper couponMapper) {
        this.couponRepository = couponRepository;
        this.couponMapper = couponMapper;
    }

    public List<CouponResponse> findCouponByMember(final Member member) {
        final Coupons coupons = couponRepository.findCouponsByMemberId(member.getId());
        return coupons.getCoupons().stream()
                .map(couponMapper::toCouponResponse)
                .collect(toList());
    }

    public List<CouponTypeResponse> findCouponAll() {
        final Coupons coupons = couponRepository.findCouponAll();
        return coupons.getCoupons().stream()
                .map(couponMapper::toCouponTypeResponse)
                .collect(toList());
    }
}
