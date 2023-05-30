package cart.application.coupon;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.dto.CouponResponse;
import cart.dto.CouponTypeResponse;
import cart.repository.coupon.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
public class CouponProvider {

    private final CouponRepository couponRepository;

    public CouponProvider(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> findCouponByMember(final Member member) {
        final Coupons coupons = couponRepository.findCouponsByMemberId(member.getId());
        return coupons.getCoupons().stream()
                .map(CouponProvider::toCouponResponse)
                .collect(toList());
    }

    private static CouponResponse toCouponResponse(final Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName().getName(),
                coupon.getDiscountAmount().getDiscountAmount(),
                coupon.getDescription().getDescription(),
                coupon.getUsageStatus().getUsageStatus());
    }

    public List<CouponTypeResponse> findCouponAll() {
        final Coupons coupons = couponRepository.findCouponAll();
        return coupons.getCoupons().stream()
                .map(CouponProvider::toCouponTypeResponse)
                .collect(toList());
    }

    private static CouponTypeResponse toCouponTypeResponse(final Coupon coupon) {
        return new CouponTypeResponse(
                coupon.getId(),
                coupon.getName().getName(),
                coupon.getDiscountAmount().getDiscountAmount(),
                coupon.getDescription().getDescription());
    }
}
