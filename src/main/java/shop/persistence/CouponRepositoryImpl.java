package shop.persistence;

import shop.domain.coupon.Coupon;
import shop.domain.coupon.CouponType;
import shop.domain.repository.CouponRepository;
import shop.exception.PersistenceException;
import shop.persistence.dao.CouponDao;
import shop.persistence.entity.CouponEntity;
import org.springframework.stereotype.Repository;

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
        CouponEntity couponEntity = couponDao.findById(id)
                .orElseThrow(() -> new PersistenceException(id + "를 갖는 쿠폰을 찾을 수 없습니다."));

        return toCoupon(couponEntity);
    }

    @Override
    public Coupon findByCouponType(CouponType couponType) {
        String name = couponType.getName();
        Integer discountRate = couponType.getDiscountRate();

        CouponEntity couponEntity = couponDao.findByNameAndDiscountRate(name, discountRate)
                .orElseThrow(() -> new PersistenceException(
                        name + "(" + discountRate + ")에 해당하는 쿠폰을 찾을 수 없습니다.")
                );

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
