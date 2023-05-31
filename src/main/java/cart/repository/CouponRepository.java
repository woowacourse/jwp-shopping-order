package cart.repository;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon save(final Coupon coupon) {
        if (Objects.isNull(coupon.getId())) {
            CouponEntity couponEntity = couponDao.insert(CouponEntity.from(coupon));
            return new Coupon(couponEntity.getId(), coupon.getName(), coupon.getDiscountPolicy(), coupon.getValue(), coupon.getMinimumPrice());
        }
        couponDao.update(CouponEntity.from(coupon));
        return coupon;
    }

    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(CouponEntity::toDomain)
                .collect(Collectors.toList());
    }
}
