package cart.persistence;

import cart.application.repository.CouponRepository;
import cart.domain.coupon.Coupon;
import cart.persistence.dao.CouponDao;
import cart.persistence.entity.CouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private final CouponDao couponDao;

    public CouponJdbcRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public long create(final Coupon coupon) {
        CouponEntity entity = CouponEntity.from(coupon);
        return couponDao.create(entity);
    }
}
