package cart.step2.coupon.service;

import cart.step2.coupon.domain.Coupon;
import cart.step2.coupon.domain.repository.CouponRepository;
import cart.step2.coupon.presentation.dto.CouponResponse;
import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponTypeRepository couponTypeRepository;

    public CouponService(final CouponRepository couponRepository, final CouponTypeRepository couponTypeRepository) {
        this.couponRepository = couponRepository;
        this.couponTypeRepository = couponTypeRepository;
    }

    @Transactional
    public Long createCoupon(final Long memberId, final Long couponTypeId) {
        return couponRepository.create(memberId, couponTypeId);
    }

    @Transactional
    public void addCoupon(final Long memberId, final Long couponId) {
        couponRepository.updateUsageStatus(memberId, couponId);
    }

    public List<CouponResponse> getMemberCoupons(final Long memberId) {
        return couponRepository.findAll(memberId).stream()
                .map(coupon -> {
                    CouponType couponType = couponTypeRepository.findById(coupon.getCouponTypeId());
                    return new CouponResponse(coupon, couponType);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByCouponId(final Long couponId) {
        validateCouponUsageStatus(couponId);
        couponRepository.deleteById(couponId);
    }

    private void validateCouponUsageStatus(final Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        if (coupon.getUsageStatus().equals("N")) {
            throw new IllegalArgumentException("사용하지 않은 쿠폰은 삭제할 수 없습니다. 사용한 후에 삭제해주세요!");
        }
    }

    @Transactional
    public void changeUsageStatus(final Long memberId, final Long couponId) {
        couponRepository.updateUsageStatus(memberId, couponId);
    }

}
