package cart.application;

import cart.dao.CouponDao;
import cart.dao.CouponTypeDao;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.dto.CouponResponse;
import cart.dto.CouponTypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CouponService {

    private final CouponDao couponDao;
    private final CouponTypeDao couponTypeDao;

    public CouponService(final CouponDao couponDao, final CouponTypeDao couponTypeDao) {
        this.couponDao = couponDao;
        this.couponTypeDao = couponTypeDao;
    }

    @Transactional
    public Long createCoupon(final Long memberId, final Long couponTypeId) {
        return couponDao.create(memberId, couponTypeId);
    }

    @Transactional
    public void addCoupon(final Long memberId, final Long couponId) {
        couponDao.updateUsageStatus(memberId, couponId);
    }

    public List<CouponTypeResponse> getCouponsType() {
        return couponTypeDao.findAll().stream()
                .map(CouponTypeResponse::new)
                .collect(Collectors.toList());
    }

    public List<CouponResponse> getMemberCoupons(final Long memberId) {
        return couponDao.findAll(memberId).stream()
                .map(coupon -> {
                    CouponType couponType = couponTypeDao.findById(coupon.getCouponTypeId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰 종류입니다. 쿠폰 타입 ID가 일치하는지 확인해주세요."));
                    return new CouponResponse(coupon, couponType);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByCouponId(final Long couponId) {
        validateCouponUsageStatus(couponId);
        couponDao.deleteById(couponId);
    }

    private void validateCouponUsageStatus(final Long couponId) {
        Coupon coupon = couponDao.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다. 쿠폰 ID가 일치하는지 확인해주세요."));
        if (coupon.getUsageStatus().equals("N")) {
            throw new IllegalArgumentException("사용하지 않은 쿠폰은 삭제할 수 없습니다. 사용한 후에 삭제해주세요!");
        }
    }

}
