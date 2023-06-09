package cart.db.repository;

import cart.db.dao.CouponDao;
import cart.db.entity.CouponEntity;
import cart.domain.coupon.Coupon;
import cart.exception.BadRequestException;
import org.springframework.stereotype.Repository;

import static cart.db.mapper.CouponMapper.toDomain;
import static cart.exception.ErrorCode.INVALID_COUPON_ID;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon findById(final Long id) {
        CouponEntity couponEntity = couponDao.findById(id)
                .orElseThrow(() -> new BadRequestException(INVALID_COUPON_ID));
        return toDomain(couponEntity);
    }
}
