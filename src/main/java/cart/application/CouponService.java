package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Money;
import cart.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Transactional
    public Money apply(Order order, Long couponId) {
        Coupon coupon = couponDao.findById(couponId);
        Money discounting = order.apply(coupon);
        couponDao.delete(couponId);
        return discounting;
    }
}
