package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon save(final Coupon coupon) {
        final CouponEntity couponEntity = CouponEntity.from(coupon);
        final CouponEntity savedCouponEntity = couponDao.insert(couponEntity);
        return new Coupon(
                savedCouponEntity.getId(),
                coupon.getName(),
                coupon.getDiscountPolicyType(),
                coupon.getDiscountValue(),
                coupon.getMinimumPrice(),
                coupon.isUsed(),
                coupon.getMemberId()
        );
    }

    public List<Coupon> findAllByUsedAndMemberId(final boolean used, final Long memberId) {
        return couponDao.findAllByUsedAndMemberId(used, memberId).stream()
                .map(CouponEntity::toDomain)
                .collect(toList());
    }

    public Optional<Coupon> findById(final Long id) {
        return couponDao.findById(id).map(CouponEntity::toDomain);
    }

    public void saveAll(final List<Coupon> coupons) {
        final List<CouponEntity> couponEntities = coupons.stream()
                .map(CouponEntity::from)
                .collect(toList());
        couponDao.insertAll(couponEntities);
    }

    public void update(final Coupon coupon) {
        final CouponEntity couponEntity = CouponEntity.from(coupon);
        couponDao.updateUsed(couponEntity);
    }
}
