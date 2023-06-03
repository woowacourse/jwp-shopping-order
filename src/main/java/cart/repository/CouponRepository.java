package cart.repository;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.DiscountPolicyType;
import cart.entity.CouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon saveCoupon(final Coupon coupon) {
        CouponEntity couponEntity = new CouponEntity(
                coupon.getName(),
                coupon.getType(),
                coupon.getDiscountPolicy().getDiscountPrice(),
                coupon.getMinimumPrice()
        );

        CouponEntity savedCouponEntity = couponDao.insert(couponEntity);
        return makeCoupon(savedCouponEntity);
    }

    private Coupon makeCoupon(final CouponEntity couponEntity) {
        DiscountPolicy discountPolicy = DiscountPolicyType.findDiscountPolicy(
                couponEntity.getPolicyType(),
                couponEntity.getDiscountValue()
        );

        return new Coupon(couponEntity.getId(), couponEntity.getName(), discountPolicy, couponEntity.getMinimumPrice());
    }
}
