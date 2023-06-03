package cart.repository;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon save(Coupon coupon) {
        Long id = couponDao.save(toEntity(coupon));
        return new Coupon(
                id,
                coupon.getName(),
                coupon.getCouponType(),
                coupon.getDiscountValue(),
                coupon.getMinOrderPrice()
        );
    }

    private CouponEntity toEntity(Coupon coupon) {
        return new CouponEntity(
                coupon.getName(),
                coupon.getCouponType().name(),
                coupon.getDiscountValue(),
                coupon.getMinOrderPrice().getValue()
        );
    }
}
