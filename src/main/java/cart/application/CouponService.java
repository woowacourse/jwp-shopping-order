package cart.application;

import cart.dao.CouponDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Transactional
    public void addCoupon(final Long memberId, final Long couponId) {
        couponDao.updateUsageStatus(memberId, couponId);
    }

}
