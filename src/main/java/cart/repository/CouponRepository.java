package cart.repository;

import cart.dao.CouponDao;
import cart.dao.CouponTypeDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.CouponTypeEntity;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import java.util.List;
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

    public Optional<Coupon> findById(final Long id) {
        final Optional<CouponEntity> foundCoupon = couponDao.findById(id);
        if (foundCoupon.isEmpty()) {
            return Optional.empty();
        }
        final CouponTypeEntity couponType = findCouponType(id);
        return Optional.of(Coupon.of(foundCoupon.get(), CouponType.from(couponType)));
    }

    public void updateStatus(final Coupon coupon) {
        couponDao.updateStatus(CouponEntity.from(coupon));
    }

    public List<Coupon> findUsableByMember(final Member member) {
        final List<CouponEntity> coupons = couponDao.findByMember(member.getId());
        return coupons.stream()
                .filter(coupon -> !coupon.isUsed())
                .map(coupon -> Coupon.of(coupon, CouponType.from(findCouponType(coupon.getCouponTypeId()))))
                .collect(Collectors.toList());
    }

    private CouponTypeEntity findCouponType(final Long id) {
        final CouponTypeEntity couponType = couponTypeDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("illegal data exists in table COUPON; coupon_type_id"));
        return couponType;
    }
}
