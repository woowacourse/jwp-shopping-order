package cart.repository;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<Coupon> findAll() {
        List<CouponEntity> couponEntities = couponDao.findAll();
        return couponEntities.stream()
                .map(CouponEntity::toCoupon)
                .collect(Collectors.toList());
    }

    public Long create(Coupon coupon) {
        return couponDao.create(CouponEntity.from(coupon));
    }

    public void delete(Long id) {
        couponDao.delete(id);
    }
}
