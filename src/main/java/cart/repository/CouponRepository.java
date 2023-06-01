package cart.repository;

import cart.dao.CouponDao;
import cart.dao.dto.CouponDto;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon insert(final Coupon coupon) {
        return couponDao.insert(CouponDto.from(coupon)).toCoupon();
    }

    public Coupon findById(final Long id) {
        return couponDao.findById(id).toCoupon();
    }
}
