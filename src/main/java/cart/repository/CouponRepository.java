package cart.repository;

import cart.dao.CouponDao;
import cart.dao.CouponTypeDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.CouponTypeEntity;
import cart.domain.Coupon;
import cart.domain.CouponType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final CouponTypeDao couponTypeDao;

    public CouponRepository(final CouponDao couponDao, final CouponTypeDao couponTypeDao) {
        this.couponDao = couponDao;
        this.couponTypeDao = couponTypeDao;
    }

    public Optional<Coupon> findByIdForMember(final long memberId, final long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        return couponDao.findByIdForMember(memberId, id)
                .map(couponEntity -> couponEntity.create(findCouponType(couponEntity.getCouponTypeId())));
    }

    public Optional<Coupon> findById(final long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        return couponDao.findById(id)
                .map(couponEntity -> couponEntity.create(findCouponType(couponEntity.getCouponTypeId())));
    }

    public void updateStatus(final long memberId, final Coupon coupon) {
        couponDao.updateStatus(CouponEntity.from(memberId, coupon));
    }

    public List<Coupon> findUsableByMember(final long memberId) {
        final List<CouponEntity> coupons = couponDao.findByMember(memberId);
        return coupons.stream()
                .filter(coupon -> !coupon.isUsed())
                .map(coupon -> coupon.create(findCouponType(coupon.getCouponTypeId())))
                .collect(Collectors.toList());
    }

    private CouponType findCouponType(final long id) {
        final CouponTypeEntity couponType = couponTypeDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("illegal data exists in table COUPON; coupon_type_id"));
        return couponType.create();
    }
}
