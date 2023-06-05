package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon findById(long couponId) {
        return couponDao.findById(couponId);
    }
}
