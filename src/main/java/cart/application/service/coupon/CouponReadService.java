package cart.application.service.coupon;

import cart.application.repository.CouponRepository;
import cart.domain.Coupon;
import cart.ui.MemberAuth;
import cart.ui.coupon.dto.CouponResponse;
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

    public List<CouponResponse> findByMember(final MemberAuth memberAuth) {
        final List<Coupon> coupons = couponRepository.findByMemberId(memberAuth.getId());
//        coupons.stream()
//                .map(coupon -> new CouponResponse(
//                        coupon.getId(),
//                        coupon.getCouponName(),
//                        coupon.getDiscountPercent(),
//                        discountAmount, coupon.getMinAmount())
//                )
//                .collect(Collectors.toList());
        return null;
    }
}
