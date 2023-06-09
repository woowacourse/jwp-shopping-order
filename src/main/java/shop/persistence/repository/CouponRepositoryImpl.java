package shop.persistence.repository;

import org.springframework.stereotype.Repository;
import shop.domain.coupon.Coupon;
import shop.domain.coupon.CouponType;
import shop.domain.repository.CouponRepository;
import shop.persistence.dao.CouponDao;
import shop.persistence.entity.CouponEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponDao couponDao;

    public CouponRepositoryImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public Long save(Coupon coupon) {
        CouponEntity couponEntity = new CouponEntity(
                coupon.getName(),
                coupon.getDiscountRate(),
                coupon.getPeriod(),
                coupon.getExpiredAt()
        );

        return couponDao.insert(couponEntity);
    }

    @Override
    public List<Coupon> findAll() {
        List<CouponEntity> allCouponEntities = couponDao.findAll();

        return allCouponEntities.stream()
                .map(this::toCoupon)
                .collect(Collectors.toList());
    }

    @Override
    public Coupon findById(Long id) {
        CouponEntity couponEntity = couponDao.findById(id);

        return toCoupon(couponEntity);
    }

    @Override
    public Coupon findByCouponType(CouponType couponType) {
        String name = couponType.getName();
        Integer discountRate = couponType.getDiscountRate();

        CouponEntity couponEntity = couponDao.findByNameAndDiscountRate(name, discountRate);

        return toCoupon(couponEntity);
    }

    private Coupon toCoupon(CouponEntity couponEntity) {
        return new Coupon(
                couponEntity.getId(),
                couponEntity.getName(),
                couponEntity.getDiscountRate(),
                couponEntity.getPeriod(),
                couponEntity.getExpiredAt()
        );
    }
}
