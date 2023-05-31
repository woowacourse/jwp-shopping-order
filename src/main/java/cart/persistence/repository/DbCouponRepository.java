package cart.persistence.repository;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.persistence.dao.CouponDao;
import cart.persistence.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbCouponRepository implements CouponRepository {
    private final CouponDao couponDao;

    public DbCouponRepository(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public List<Coupon> findAll() {
        List<CouponEntity> couponEntities = couponDao.findAll();
        return couponEntities.stream()
                .map(this::mapToCoupon)
                .collect(Collectors.toList());
    }

    @Override
    public Coupon findById(Long id) {
        CouponEntity couponEntity = couponDao.findById(id);
        return mapToCoupon(couponEntity);
    }

    @Override
    public Long add(Coupon coupon) {
        return couponDao.add(mapToCouponEntity(coupon));
    }

    @Override
    public void delete(Long id) {
        couponDao.delete(id);
    }

    public CouponEntity mapToCouponEntity(Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountType().getName(),
                coupon.getDiscountPercent(),
                coupon.getDiscountAmount(),
                coupon.getMinimumPrice());
    }

    public Coupon mapToCoupon(CouponEntity couponEntity) {
        return CouponMapper.mapToCoupon(couponEntity);
    }
}
