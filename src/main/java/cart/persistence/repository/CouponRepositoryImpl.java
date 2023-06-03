package cart.persistence.repository;

import static cart.persistence.mapper.CouponMapper.convertCoupon;
import static cart.persistence.mapper.CouponMapper.convertCouponEntity;
import static cart.persistence.mapper.CouponMapper.convertCouponWithId;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.CouponWithId;
import cart.exception.DBException;
import cart.exception.ErrorCode;
import cart.exception.NotFoundException;
import cart.persistence.dao.CouponDao;
import cart.persistence.entity.CouponEntity;
import cart.persistence.mapper.CouponMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponDao couponDao;

    public CouponRepositoryImpl(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public List<CouponWithId> findAll() {
        return couponDao.getAllCoupons().stream()
            .map(CouponMapper::convertCouponWithId)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Coupon findById(final Long id) {
        final CouponEntity couponEntity = couponDao.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        });
        return convertCoupon(couponEntity);
    }

    @Override
    public boolean existByNameAndDiscountRate(final String name, final int discountRate) {
        return couponDao.existByNameAndDiscountRate(name, discountRate);
    }

    @Override
    public long insert(final Coupon coupon) {
        final CouponEntity couponEntity = convertCouponEntity(coupon);
        return couponDao.insert(couponEntity);
    }

    @Override
    public void deleteById(final Long couponId) {
        final int deletedCount = couponDao.deleteById(couponId);
        if (deletedCount != 1) {
            throw new DBException(ErrorCode.DB_DELETE_ERROR);
        }
    }

    @Override
    public CouponWithId findByNameAndDiscountRate(final String name, final int discountRate) {
        final CouponEntity couponEntity = couponDao.findByNameAndDiscountRate(name, discountRate)
            .orElseThrow(() -> {
                throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
            });
        return convertCouponWithId(couponEntity);
    }
}
