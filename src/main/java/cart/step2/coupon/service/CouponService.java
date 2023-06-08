package cart.step2.coupon.service;

import cart.step2.coupon.domain.Coupon;
import cart.step2.coupon.domain.repository.CouponRepository;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Coupon> getMemberCoupons(final Long memberId) {
        return couponRepository.findAll(memberId);
    }

    @Transactional
    public void deleteByCouponId(final Long couponId) {
        validateCouponUsageStatus(couponId);
        couponRepository.deleteById(couponId);
    }

    private void validateCouponUsageStatus(final Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        coupon.validateUsageStatus();
    }

    @Transactional
    public void changeUsageStatus(final Long memberId, final Long couponId) {
        couponRepository.updateUsageStatus(memberId, couponId);
    }

}
