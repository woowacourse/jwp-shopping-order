package cart.db.repository;

import cart.db.dao.CouponDao;
import cart.db.entity.CouponEntity;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Repository;

import static cart.db.mapper.CouponMapper.toDomain;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon findById(final Long id) {
        CouponEntity couponEntity = couponDao.findById(id);
        return toDomain(couponEntity);
    }
}
