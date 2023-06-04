package cart.repository;

import cart.dao.CouponDao;
import cart.dao.entity.CouponEntity;
import cart.domain.Coupon;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {
    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon findById(final Long id) {
        CouponEntity couponEntity = couponDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰 존재하지 않습니다."));
        return couponEntity.toCoupon();
    }

    public List<Coupon> findAllCoupons() {
        List<CouponEntity> allCouponEntity = couponDao.findAll();
        return allCouponEntity.stream()
                .map(CouponEntity::toCoupon)
                .collect(Collectors.toList());
    }
}
