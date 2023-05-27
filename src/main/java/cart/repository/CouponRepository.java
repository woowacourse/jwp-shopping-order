package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.CouponDao;
import cart.domain.coupon.AmountDiscountPolicy;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeliveryFeeDiscountPolicy;
import cart.domain.coupon.DiscountCondition;
import cart.domain.coupon.DiscountConditionType;
import cart.domain.coupon.DiscountPolicy;
import cart.domain.coupon.DiscountPolicyType;
import cart.domain.coupon.MinimumPriceDiscountCondition;
import cart.domain.coupon.NoneDiscountCondition;
import cart.domain.coupon.PercentDiscountPolicy;
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
                coupon.getDiscountPolicy(),
                coupon.getDiscountCondition()
        );
    }

    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(this::toCoupon)
                .collect(toList());
    }

    public Coupon toCoupon(final CouponEntity couponEntity) {
        final DiscountPolicy discountPolicy = parseDiscountPolicy(couponEntity);
        final DiscountCondition discountCondition = parseDiscountCondition(couponEntity);
        return new Coupon(couponEntity.getId(), couponEntity.getName(), discountPolicy, discountCondition);
    }

    private DiscountCondition parseDiscountCondition(final CouponEntity couponEntity) {
        final DiscountConditionType conditionType = DiscountConditionType.from(couponEntity.getConditionType());
        if (conditionType == DiscountConditionType.MINIMUM_PRICE) {
            return new MinimumPriceDiscountCondition(couponEntity.getMinimumPrice());
        }
        return new NoneDiscountCondition();
    }

    private DiscountPolicy parseDiscountPolicy(final CouponEntity couponEntity) {
        final DiscountPolicyType policyType = DiscountPolicyType.from(couponEntity.getPolicyType());
        if (policyType == DiscountPolicyType.PRICE) {
            return new AmountDiscountPolicy(couponEntity.getDiscountPrice());
        }
        if (policyType == DiscountPolicyType.PERCENT) {
            return new PercentDiscountPolicy(couponEntity.getDiscountPercent());
        }
        return new DeliveryFeeDiscountPolicy();
    }

    public Optional<Coupon> findById(final Long id) {
        return couponDao.findById(id)
                .map(this::toCoupon);
    }
}
